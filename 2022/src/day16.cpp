#include <unordered_set>

#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day16.test";
constexpr unsigned TIME = 30;
const string START = "AA";

struct ValveInfo {
  int rate;
  vector<string> children;
  unordered_set<string> visitedChildren;
};

void parseInput(const lines_t &lines,
                unordered_map<string, ValveInfo> &valveToInfo) {
  for (auto &line : lines) {
    auto [valveRate, children] = str::split(line, ';');
    auto [valve, rateStr] = str::split(valveRate, '=');
    auto childrenVec = str::splitAll(children, ",");
    valveToInfo.try_emplace(valve, ValveInfo{stoi(rateStr), childrenVec, {}});
  }
}

// on the current valve, but haven't opened it yet
int dfs(unordered_map<string, ValveInfo> &valveToInfo, string current,
        int time) {
  int pressure = 0;

  if (time < 2) {
    return pressure;
  }

  auto &info = valveToInfo.at(current);
  auto originalRate = info.rate;
  if (originalRate) {
    --time; // a minute to open
    // pressure += time * originalRate;
    // UserPrint::print(' ', "Current:", current, "Time:", time,
    //                  "Pressure:", pressure);
    info.rate = 0;
  }

  auto &children = info.children;
  int maxP = 0;
  string maxChild;
  for (auto &child : children) {
    if (info.visitedChildren.count(child)) {
      continue;
    } else {
      info.visitedChildren.insert(child);
    }
    // a minute to go to next valve
    int val = dfs(valveToInfo, child, time - 1);
    if (val > maxP) {
      maxChild = child;
    }
    maxP = max(val, maxP);
    info.visitedChildren.erase(child);
  }
  // assign back the originalRate
  info.rate = originalRate;
  pressure += maxP;
  // UserPrint::print(' ', "Current:", current, "Time:", time, "Child:",
  // maxChild,
  //                  "Pressure:", pressure);
  return pressure;
}

int main() {
  lines_t lines = getInput(INPUT);
  unordered_map<string, ValveInfo> valveToInfo;
  parseInput(lines, valveToInfo);

  auto res1 = dfs(valveToInfo, START, TIME);
  UserPrint::print(' ', "Part 1 Result:", res1);

  // auto res2 = part2(rocks);
  // UserPrint::print(' ', "Part 2 Result:", res2);
}