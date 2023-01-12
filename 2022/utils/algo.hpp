#include <functional>
#include <iostream>
#include <numeric>
#include <set>
#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

namespace vec {
template <typename T, typename Compare>
void sortVec(vector<T> &v, const Compare &comp) {
  sort(v.begin(), v.end(), comp);
}

template <typename T> void print(const vector<T> &v, char sep = ' ') {
  for (auto elm : v) {
    cout << elm << sep;
  }
  cout << endl;
}

template <typename T, typename ResT, typename AccT>
ResT accu(const vector<T> &v, ResT init, AccT op) {
  return accumulate(v.begin(), v.end(), init, op);
}

} // namespace vec

namespace str {
// convert a string into a set of chars
inline set<char> convertStrToSet(const string &str) {
  set<char> res;
  for (const char &c : str) {
    res.insert(c);
  }
  return res;
}

// convert a string into a vector of chars
inline vector<char> strToVec(const string &str) {
  vector<char> res;
  for (const char &c : str) {
    res.push_back(c);
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

inline vector<string> splitAll(const string &str, const string &sep) {
  vector<string> res;
  size_t startPos = 0;
  auto pos = str.find(sep, startPos);
  while (pos != string::npos) {
    res.push_back(str.substr(startPos, pos - startPos));
    startPos = pos + sep.size();
    pos = str.find(sep, startPos);
  }
  res.push_back(str.substr(startPos));
  return res;
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

template <typename ElmT, typename Func1>
void iterateMatrixByIdx(const vector<vector<ElmT>> &matrix,
                        Func1 &&perElmFunc) {
  for (int r = 0; r < matrix.size(); ++r) {
    for (int c = 0; c < matrix[r].size(); ++c) {
      perElmFunc(matrix, r, c);
    }
  }
}

template <typename ElmT> void printMatrix(const vector<vector<ElmT>> &matrix) {
  auto print = [](ElmT val) { cout << val << ' '; };
  auto printNewLine = [](vector<ElmT> _) { cout << '\n'; };
  iterateMatrix(matrix, print, printNewLine);
  cout << '\n';
}
} // namespace matrix

namespace direction {
using coord_t = pair<int, int>;

enum class Direction : char {
  eUp = 'U',
  eDown = 'D',
  eLeft = 'L',
  eRight = 'R'
};

// Move point in Direction [d] by [step]
// [point] represents a pair of coordinates, i.e. (x, y)
template <Direction d> void move_point(coord_t &point, int step) {
  if constexpr (d == Direction::eUp) {
    point.second += step;
  } else if constexpr (d == Direction::eDown) {
    point.second -= step;
  } else if constexpr (d == Direction::eLeft) {
    point.first -= step;
  } else if constexpr (d == Direction::eRight) {
    point.first += step;
  }
}

// Move point in Direction [d] by [step]
// [point] represents a pair of coordinates, i.e. (x, y)
template <Direction d>
[[nodiscard]] coord_t move_point(const coord_t &point, int step) {
  coord_t res = point;
  if constexpr (d == Direction::eUp) {
    res.second += step;
  } else if constexpr (d == Direction::eDown) {
    res.second -= step;
  } else if constexpr (d == Direction::eLeft) {
    res.first -= step;
  } else if constexpr (d == Direction::eRight) {
    res.first += step;
  }
  return res;
}

} // namespace direction

namespace UserPrint {
template <typename... ArgTypes> void print(char sep, ArgTypes... args);

template <typename T> inline void print(char, T t) { cout << t << endl; }

template <typename T, typename... ArgTypes>
void print(char sep, T t, ArgTypes... args) {
  cout << t << sep;
  print(sep, args...);
}
} // namespace UserPrint