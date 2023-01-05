#include <algorithm>
#include <set>

#include "utils/algo.hpp"
#include "utils/input.hpp"
#include "utils/tree.hpp"

using namespace std;

const string INPUT = "../inputs/day7.txt";
constexpr char CMD_PREFIX = '$';
const string DIR_PREFIX = "dir";
const string CMD_CD = "cd";
const string CMD_LS = "ls";
const string DIR_LAST = "..";
const string ROOT = "/";
const string SEP = " ";
constexpr int VAL_THRESHOLD = 100000;
constexpr int TOTAL_SPACE = 70000000;
constexpr int MIN_UNUSED_REQ = 30000000;

pair<int, int> process(lines_t lines) {
  Tree tree(0, ROOT);
  auto *root = tree.getRoot();
  Node *currNode = root;
  for (const auto &line : lines) {
    if (line.at(0) == CMD_PREFIX) {
      string cmdline = line.substr(2);
      string cmd = cmdline.substr(0, 2);
      if (cmd == CMD_CD) {
        // cd
        auto dir = split(cmdline, SEP).second;
        if (dir == DIR_LAST) {
          currNode = currNode->parent;
        } else {
          if (dir == ROOT) {
            continue;
          }
          currNode = currNode->children.at(dir);
        }
      }
    } else {
      // in ls
      if (line.size() > DIR_PREFIX.size() &&
          line.substr(0, DIR_PREFIX.size()) == DIR_PREFIX) {
        // sub dir
        auto dirName = split(line, SEP).second;
        tree.addChild(0, dirName, currNode);
      } else {
        // file
        auto [size, filename] = split(line, SEP);
        // add only file sizes for now, traverse dirs later
        currNode->val += stol(size);
      }
    }
  }
  auto get_total_size = [](Node *curr) {
    for (auto [_, child] : curr->children) {
      curr->val += child->val;
    }
  };
  // Update node val to be total size of the directory
  tree.traverse_and_apply_post(root, get_total_size);

  // part 1
  int result_p1 = 0;
  auto add_val_under_threshold = [](Node *curr, int &acc) {
    acc += curr->val <= VAL_THRESHOLD ? curr->val : 0;
  };
  tree.traverse_and_acc(root, result_p1, add_val_under_threshold);

  // part 2
  int diff = root->val - (TOTAL_SPACE - MIN_UNUSED_REQ);
  // debug
  cout << "Root has size " << root->val << " we need to free " << diff << endl;
  assert(diff > 0);
  int result_p2 = root->val;
  auto find_smallest_bigger_than = [&diff](Node *curr, int &acc) {
    acc = curr->val >= diff && curr->val < acc ? curr->val : acc;
  };
  tree.traverse_and_acc(root, result_p2, find_smallest_bigger_than);

  return {result_p1, result_p2};
}

int main() {
  lines_t lines = getInput(INPUT);
  auto [res1, res2] = process(lines);
  cout << "Part 1 result: " << res1 << endl;
  cout << "Part 2 result: " << res2 << endl;
}
