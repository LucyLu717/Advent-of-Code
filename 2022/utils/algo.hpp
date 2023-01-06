#include <iostream>
#include <set>
#include <string>
#include <unordered_map>

using namespace std;

namespace str {
// convert a string into a set of chars
inline set<char> convertStrToSet(const string &str) {
  set<char> res;
  for (const char &c : str) {
    res.insert(c);
  }
  return res;
}

// split a string around the first occurrence of a given character
inline pair<string, string> split(const string &str, char sep) {
  auto pos = str.find(sep);
  if (string::npos == pos) {
    throw runtime_error("no sep " + string(1, sep) + " found in str " + str);
  }
  return make_pair(str.substr(0, pos), str.substr(pos + 1));
}

// split a string around the first occurrence of a given string pattern
inline pair<string, string> split(const string &str, const string &sep) {
  auto pos = str.find(sep);
  if (string::npos == pos) {
    throw runtime_error("no sep " + sep + " found in str " + str);
  }
  return make_pair(str.substr(0, pos), str.substr(pos + sep.size()));
}
} // namespace str

namespace matrix {
// rotate a square matrix 90 degree clockwise

namespace detail {
using coord_t = pair<int, int>;
} // namespace detail

using converter_t = std::function<detail::coord_t(detail::coord_t, int)>;
inline converter_t original_coord = [](detail::coord_t pair, int matrixSize) {
  return pair;
};

inline converter_t cw90_coord = [](detail::coord_t pair,
                                   int matrixSize) -> detail::coord_t {
  return {matrixSize - 1 - pair.second, pair.first};
};

inline converter_t cw180_coord = [](detail::coord_t pair,
                                    int matrixSize) -> detail::coord_t {
  return {matrixSize - 1 - pair.first, matrixSize - 1 - pair.second};
};

inline converter_t cw270_coord = [](detail::coord_t pair,
                                    int matrixSize) -> detail::coord_t {
  return {pair.second, matrixSize - 1 - pair.first};
};

template <typename ElmT> void rotate90Clockwise(vector<vector<ElmT>> &matrix) {
  // assert the matrix is a square
  int N = matrix.size();
  assert(N);
  assert(N == matrix[0].size());
  // Traverse each cycle
  for (int i = 0; i < N / 2; i++) {
    for (int j = i; j < N - i - 1; j++) {
      // Swap elements of each cycle in clockwise direction
      int temp = matrix[i][j];
      matrix[i][j] = matrix[N - 1 - j][i];
      matrix[N - 1 - j][i] = matrix[N - 1 - i][N - 1 - j];
      matrix[N - 1 - i][N - 1 - j] = matrix[j][N - 1 - i];
      matrix[j][N - 1 - i] = temp;
    }
  }
}

template <typename ElmT, typename Func>
void iterateMatrix(const vector<vector<ElmT>> &matrix, Func &&perElmFunc) {
  for (const auto &row : matrix) {
    for (const auto &elm : row) {
      perElmFunc(elm);
    }
  }
}

template <typename ElmT, typename Func1, typename Func2>
void iterateMatrix(const vector<vector<ElmT>> &matrix, Func1 &&perElmFunc,
                   Func2 &&postRowFunc) {
  for (const auto &row : matrix) {
    for (const auto &elm : row) {
      perElmFunc(elm);
    }
    postRowFunc(row);
  }
}

template <typename ElmT> void printMatrix(const vector<vector<ElmT>> &matrix) {
  auto print = [](ElmT val) { cout << val << " "; };
  auto printNewLine = [](vector<ElmT> _) { cout << '\n'; };
  iterateMatrix(matrix, print, printNewLine);
  cout << '\n';
}
} // namespace matrix