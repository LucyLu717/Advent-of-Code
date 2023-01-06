#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day8.txt";
template <typename T> using forest_t = vector<vector<T>>;

void iterate_p1(const forest_t<char> &forest, forest_t<bool> &result,
                matrix::converter_t &converter) {
  int size = forest.size();
  for (int r = 1; r < size - 1; ++r) {
    int rowMax = forest[r][0];
    for (int c = 1; c < size - 1; ++c) {
      if (forest[r][c] > rowMax) {
        auto [r_ori, c_ori] = converter({r, c}, size);
        result[r_ori][c_ori] = true;
        rowMax = forest[r][c];
      }
    }
  }
}

void iterate_p2(const forest_t<char> &forest, forest_t<int> &result,
                matrix::converter_t &converter) {
  int size = forest.size();
  for (int r = 0; r < size; ++r) {
    for (int c = 0; c < size; ++c) {
      auto [r_ori, c_ori] = converter({r, c}, size);
      int i = c - 1;
      for (; i >= 0; --i) {
        if (forest[r][i] >= forest[r][c]) {
          result[r_ori][c_ori] *= c - i;
          break;
        }
      }
      if (i < 0) {
        result[r_ori][c_ori] *= c;
      }
    }
  }
}

template <typename Func1, typename Func2, typename ResT>
void solution(const lines_t &lines, Func1 &&iterate, Func2 &&acc, ResT defVal) {
  forest_t<char> forest;
  forest_t<ResT> result;
  for (const auto &line : lines) {
    vector<char> row;
    copy(line.begin(), line.end(), back_inserter(row));
    // assume all trees are invisible at first
    vector<ResT> res_row(row.size(), defVal);
    result.push_back(res_row);
    forest.push_back(row);
  }
  iterate(forest, result, matrix::original_coord);

  matrix::rotate90Clockwise(forest);
  iterate(forest, result, matrix::cw90_coord);

  matrix::rotate90Clockwise(forest);
  iterate(forest, result, matrix::cw180_coord);

  matrix::rotate90Clockwise(forest);
  iterate(forest, result, matrix::cw270_coord);

  matrix::iterateMatrix(result, acc);
}

int part1(const lines_t &lines) {
  int counter = 0;
  auto count_true = [&counter](bool elm) { counter += elm == true; };
  solution(lines, iterate_p1, count_true, false);
  return counter + 4 * (lines.size() - 1);
}

int part2(const lines_t &lines) {
  int maxScore = 0;
  auto count_true = [&maxScore](int elm) {
    maxScore = elm > maxScore ? elm : maxScore;
  };
  solution(lines, iterate_p2, count_true, 1);
  return maxScore;
}

int main() {
  lines_t lines = getInput(INPUT);
  // part 1
  cout << "Part 1 Result: " << part1(lines) << endl;
  // part 2
  cout << "Part 2 Result: " << part2(lines) << endl;
}
