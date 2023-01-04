#include <algorithm>
#include <set>

#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day6.txt";
constexpr int RANGE_SIZE = 4;
constexpr int RANGE_SIZE_2 = 14;

bool distinctCharsInRange(int currPos, int endPos, const vector<char> &chars) {
  set<char> counter;
  assert(currPos < endPos && endPos < chars.size());
  for (int i = currPos; i < endPos; ++i) {
    counter.insert(chars[i]);
  }
  return counter.size() == endPos - currPos;
}

int findPos(const vector<char> &chars, int rangeSize) {
  for (int i = 0; i < chars.size() - rangeSize; ++i) {
    if (distinctCharsInRange(i, i + rangeSize, chars)) {
      return i + rangeSize;
    }
  }
  return -1;
}

int main() {
  lines_t input = getInput(INPUT);
  vector<char> chars;
  copy(input[0].begin(), input[0].end(), back_inserter(chars));
  ;

  // part 1
  int charPos1 = findPos(chars, RANGE_SIZE);
  cout << "Part 1 Position: " << charPos1 << endl;

  // part 2
  int charPos2 = findPos(chars, RANGE_SIZE_2);
  cout << "Part 2 Position: " << charPos2 << endl;
}
