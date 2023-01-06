#include <algorithm>

#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day4.txt";
using pair_t = pair<int, int>;

bool fully_cover(pair_t p1, pair_t p2) {
  return (p1.first <= p2.first && p2.second <= p1.second) ||
         (p2.first <= p1.first && p1.second <= p2.second);
}

bool overlap(pair_t p1, pair_t p2) {
  return (p1.first <= p2.second && p2.first <= p1.second) ||
         (p2.second <= p1.first && p1.second <= p2.first);
}

pair_t parse_range(const string &range) {
  auto numbers = str::split(range, '-');
  return make_pair(stoi(numbers.first), stoi(numbers.second));
}

template <typename Func> int solution(const lines_t &pairs, Func &&func) {
  int count = 0;
  for (const auto &pair : pairs) {
    auto ranges = str::split(pair, ',');
    bool yes = func(parse_range(ranges.first), parse_range(ranges.second));
    count += yes ? 1 : 0;
  }
  return count;
}

int part1(const lines_t &pairs) { return solution(pairs, fully_cover); }

int part2(const lines_t &pairs) { return solution(pairs, overlap); }

int main() {
  lines_t pairs = getInput(INPUT);

  // part 1
  int score1 = part1(pairs);
  cout << "Part 1 Score: " << score1 << endl;

  // part 2
  int score2 = part2(pairs);
  cout << "Part 2 Score: " << score2 << endl;
}
