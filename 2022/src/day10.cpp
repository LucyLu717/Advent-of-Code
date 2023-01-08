#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day10.txt";

const string NOOP = "noop";
const string ADDX = "addx";
constexpr int ADD_CYCLES = 2;
const vector<int> ROUNDS = {20, 60, 100, 140, 180, 220};
constexpr int CRT_WIDTH = 40;

void draw(int cycle, int registerVal, int) {
  int pos = cycle - 1;
  if (pos % CRT_WIDTH == 0) {
    cout << endl;
  }
  if (registerVal - 1 <= pos % CRT_WIDTH &&
      pos % CRT_WIDTH <= registerVal + 1) {
    cout << '#';
  } else {
    cout << '.';
  }
}

void check_signal(int cycle, int registerVal, int &res) {
  if (find(ROUNDS.begin(), ROUNDS.end(), cycle) != ROUNDS.end()) {
    res += registerVal * cycle;
  }
}

template <typename Func> int solution(const lines_t &lines, Func &&func) {
  int registerVal = 1;
  int cycle = 0;
  int res = 0;
  for (const auto &line : lines) {
    if (line.substr(0, NOOP.size()) == NOOP) {
      func(++cycle, registerVal, res);
    } else if (line.substr(0, ADDX.size()) == ADDX) {
      auto [_, numS] = str::split(line, " ");
      auto num = stol(numS);
      for (int i = 0; i < ADD_CYCLES; ++i) {
        func(++cycle, registerVal, res);
      }
      registerVal += num;
    }
  }
  return res;
}

int main() {
  lines_t lines = getInput(INPUT);
  auto res1 = solution(lines, check_signal);
  cout << "Part 1 Result: " << res1 << endl;
  solution(lines, draw);
}
