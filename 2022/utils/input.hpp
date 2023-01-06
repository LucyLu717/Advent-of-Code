#include <fstream>
#include <iostream>
#include <string>
#include <vector>

using namespace std;
using lines_t = vector<string>;

inline lines_t getInput(const string &filen) {
  ifstream input(filen);
  if (!input.is_open()) {
    throw runtime_error("input invalid");
  }

  // process input
  string line;
  lines_t result;
  while (getline(input, line)) {
    result.push_back(line);
  }
  return result;
}
