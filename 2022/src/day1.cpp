#include <algorithm>
#include <fstream>
#include <iostream>
#include <numeric>
#include <string>
#include <vector>

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
  sort(calories.begin(), calories.end(), greater<int>());
  cout << "max calories: " << calories[0] << endl;
  cout << "top three calories total: "
       << accumulate(calories.begin(), calories.begin() + 3,
                     decltype(calories)::value_type(0))
       << endl;
}
