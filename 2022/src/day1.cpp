#include <algorithm>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

#include "utils/algo.hpp"

using namespace std;

const string INPUT = "../inputs/day1.txt";

int main() {
  ifstream input(INPUT);
  if (!input.is_open()) {
    throw runtime_error("input invalid");
  }

  // process input
  string num;
  int elfIdx = 1;
  int currCalories;
  vector<int> calories;
  while (getline(input, num)) {
    if (num.size()) {
      currCalories += stoi(num);
    } else {
      // new elf
      calories.push_back(currCalories);
      currCalories = 0;
      ++elfIdx;
      continue;
    }
  }
  cout << "total elves: " << elfIdx << endl;

  // find result
  vec::sortVec(calories, greater<int>());
  cout << "max calories: " << calories[0] << endl;
  cout << "top three calories total: "
       << vec::accu(calories, 0, std::plus<int>()) << endl;
}
