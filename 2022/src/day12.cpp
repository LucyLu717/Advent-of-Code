#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;
using point_t = direction::coord_t;
using matrix_t = vector<vector<char>>;

const string INPUT = "../inputs/day12.txt";
constexpr char START = 'S';
constexpr char END = 'E';
constexpr char LOWEST = 'a';
constexpr char HIGHEST = 'z';

pair<point_t, point_t> prep_p1(const lines_t &lines, matrix_t &matrix) {
  for (const auto &line : lines) {
    auto line_v = str::strToVec(line);
    matrix.push_back(line_v);
  }

  point_t start, end;
  auto findPoints = [&start, &end](const matrix_t &matrix, int r, int c) {
    if (matrix[r][c] == START) {
      start.first = r;
      start.second = c;
    } else if (matrix[r][c] == END) {
      end.first = r;
      end.second = c;
    }
  };
  matrix::iterateMatrixByIdx(matrix, findPoints);
  matrix[start.first][start.second] = LOWEST;
  matrix[end.first][end.second] = HIGHEST;
  return {start, end};
}

set<point_t> prep_p2(const matrix_t &matrix) {
  set<point_t> res;
  auto findPoints = [&res](const matrix_t &matrix, int r, int c) {
    if (matrix[r][c] == LOWEST) {
      res.insert({r, c});
    }
  };
  matrix::iterateMatrixByIdx(matrix, findPoints);
  return res;
}

// BFS
int findPath(const matrix_t &matrix, const point_t &start, const point_t &end) {
  int length = 0;
  set<point_t> visited;
  vector<point_t> currLvl = {start};
  vector<point_t> tmp;

  auto add_next = [&tmp, &visited, &matrix](const point_t &next, char curr) {
    if (next.first < 0 || next.second < 0 || next.first >= matrix.size() ||
        next.second >= matrix[0].size()) {
      return;
    }
    if (matrix[next.first][next.second] - curr <= 1 &&
        visited.find(next) == visited.end()) {
      tmp.push_back(next);
    }
  };

  while (!currLvl.empty()) {
    for (const auto &point : currLvl) {
      if (visited.find(point) != visited.end()) {
        continue;
      } else if (point == end) {
        return length;
      }
      visited.insert(point);
      char curr = matrix[point.first][point.second];
      add_next(direction::move_point<direction::Direction::eUp>(point, 1),
               curr);
      add_next(direction::move_point<direction::Direction::eDown>(point, 1),
               curr);
      add_next(direction::move_point<direction::Direction::eLeft>(point, 1),
               curr);
      add_next(direction::move_point<direction::Direction::eRight>(point, 1),
               curr);
    }
    currLvl = tmp;
    tmp.clear();
    ++length;
  }
  return 0;
}

int findShortestPath(const matrix_t &matrix, const set<point_t> &starts,
                     const point_t &end) {
  int shortest = numeric_limits<int>::max();
  for (const auto &start : starts) {
    auto length = findPath(matrix, start, end);
    shortest = length < shortest && length > 0 ? length : shortest;
  }
  return shortest;
}

int main() {
  lines_t lines = getInput(INPUT);
  matrix_t matrix;
  auto [start, end] = prep_p1(lines, matrix);
  UserPrint::print(' ', "Start point:", start.first, start.second);
  UserPrint::print(' ', "End point:", end.first, end.second);

  auto res1 = findPath(matrix, start, end);
  UserPrint::print(' ', "Part 1 Result:", res1);

  auto starts = prep_p2(matrix);
  auto res2 = findShortestPath(matrix, starts, end);
  UserPrint::print(' ', "Part 2 Result:", res2);
}
