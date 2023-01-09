#include <algorithm>
#include <deque>
#include <sstream>

#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day5.txt";
using pair_t = pair<int, int>;
using queue_t = deque<char>;
using queues_t = vector<queue_t>;

queue_t parse_queue(const lines_t &lines, size_t pos) {
  queue_t queue;
  for (auto it = lines.rbegin(); it != lines.rend(); ++it) {
    string line = *it;
    if (pos < line.size()) {
      char letter = line.at(pos);
      if (letter != ' ') {
        queue.push_back(letter);
      } else {
        break;
      }
    } else {
      break;
    }
  }
  return queue;
}

pair_t parse_move_step(const string &step, int &num) {
  auto instructions = str::split(step, " from ");
  num = stoi(str::split(instructions.first, ' ').second);
  auto indices = str::split(instructions.second, " to ");
  return make_pair(stoi(indices.first), stoi(indices.second));
}

void part1func(queue_t &q1, queue_t &q2, int num_to_move) {
  while (num_to_move-- != 0) {
    q2.push_back(q1.back());
    q1.pop_back();
  }
}

void part2func(queue_t &q1, queue_t &q2, int num_to_move) {
  queue_t tmp;
  part1func(q1, tmp, num_to_move);
  part1func(tmp, q2, num_to_move);
}

template <typename Func>
void apply_instructions(const lines_t &lines, queues_t &queues, Func &&func) {
  for (const auto &line : lines) {
    if (line.size() == 0 || line.at(0) != 'm') {
      continue;
    }
    int num_to_move = 0;
    pair_t queue_p = parse_move_step(line, num_to_move);
    auto &q1 = queues.at(queue_p.first - 1);
    auto &q2 = queues.at(queue_p.second - 1);
    func(q1, q2, num_to_move);
  }
}

string get_result(const queues_t &queues) {
  stringstream ss;
  for (const auto &q : queues) {
    ss << q.back();
  }
  return ss.str();
}

string part1(const lines_t &lines, queues_t queues) {
  apply_instructions(lines, queues, part1func);
  return get_result(queues);
}

string part2(const lines_t &lines, queues_t queues) {
  apply_instructions(lines, queues, part2func);
  return get_result(queues);
}

int main() {
  try {
    lines_t lines = getInput(INPUT);

    lines_t stacks;
    int num_stacks = 0;
    for (const auto &line : lines) {
      if (line.size() != 0 && line.at(1) != '1') {
        stacks.push_back(line);
      } else {
        const auto &last_line = stacks.back();
        num_stacks = count(last_line.begin(), last_line.end(), '[');
        break;
      }
    }

    // parse stacks
    queues_t queues;
    for (int i = 1; i <= num_stacks; ++i) {
      queues.push_back(parse_queue(stacks, (i - 1) * 4 + 1));
    }

    // part 1
    string res1 = part1(lines, queues);
    cout << "Part 1 Result: " << res1 << endl;

    // part 2
    string res2 = part2(lines, queues);
    cout << "Part 2 Result: " << res2 << endl;
  } catch (exception &ex) {
    cout << ex.what() << endl;
  }
}
