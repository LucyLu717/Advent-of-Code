#include <iostream>
#include <memory>
#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

struct Node {
  int val = 0;
  string name;
  Node *parent = nullptr;
  unordered_map<string, Node *> children; // name to object
};

class Tree {
public:
  Tree(int rootVal, string name) {
    root_ = make_unique<Node>();
    root_->val = rootVal;
    root_->name = name;
  }

  Node *getRoot() const { return root_.get(); }

  // Returns a pointer to the new created child
  void addChild(int val, const string &name, Node *parent) {
    unique_ptr<Node> child = make_unique<Node>();
    child->val = val;
    child->name = name;
    child->parent = parent;
    auto [_, insertSuccess] = parent->children.try_emplace(name, child.get());
    if (!insertSuccess) {
      cout << "WARN: child " << name << " already exists under parent "
           << parent->name << endl;
    }
    nodes_.push_back(std::move(child));
    return;
  }

  // traverse methods

  template <typename Func>
  void traverse_and_apply_post(Node *start, Func &&func) {
    if (start->children.empty()) {
      return;
    }
    for (auto &[_, child] : start->children) {
      traverse_and_apply_post(child, func);
      func(child);
    }
    if (!start->parent) {
      func(start);
    }
  }

  template <typename Func, typename AccT>
  void traverse_and_acc(Node *start, AccT &initVal, Func &&func) {
    if (!start->parent) {
      func(start, initVal);
    }
    if (start->children.empty()) {
      return;
    }
    for (auto &[_, child] : start->children) {
      func(child, initVal);
      traverse_and_acc(child, initVal, func);
    }
  }

private:
  unique_ptr<Node> root_;
  vector<unique_ptr<Node>> nodes_;
};