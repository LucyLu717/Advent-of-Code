#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

using point_t = direction::coord_t;
using range_t = pair<int, int>;
using info_t = vector<pair<point_t, point_t>>;

const string INPUT = "../inputs/day15.txt";
constexpr int rowY = 2000000;
constexpr int beaconLimit = 4000000;

info_t parseInput(const lines_t &lines) {
  info_t info;
  for (const auto &line : lines) {
    auto [sensorS, beaconS] = str::split(line, ':');
    auto sensorP = str::split(sensorS, ',');
    auto beaconP = str::split(beaconS, ',');
    info.emplace_back(
        make_pair<int, int>(stol(sensorP.first), stol(sensorP.second)),
        make_pair<int, int>(stol(beaconP.first), stol(beaconP.second)));
  }
  return info;
}

// debug
void printRanges(const set<range_t> &ranges) {
  for (auto &range : ranges) {
    UserPrint::print(' ', "ranges", range.first, range.second);
  }
  cout << '\n';
}

set<range_t> mergeTouchingRanges(const set<range_t> &ranges) {
  set<range_t> res;
  range_t current = *ranges.begin();
  for (auto i = ranges.begin(); i != ranges.end(); ++i) {
    // current vs i + 1
    if (next(i) != ranges.end() && current.second >= next(i)->first) {
      current = {current.first, max(next(i)->second, current.second)};
    } else {
      // current is stable, add to res
      res.emplace(current.first, current.second);
      current = *next(i);
    }
  }
  return res;
}

// Check existing ranges, update an overlapping range or insert new one
void checkAndInsert(set<range_t> &ranges, int x1, int x2) {
  for (auto i = ranges.begin(); i != ranges.end(); ++i) {
    auto &range = const_cast<range_t &>(*i);
    bool overlapOrTouch = (x2 >= range.first && x2 <= range.second) ||
                          (x1 >= range.first && x1 <= range.second) ||
                          (x1 <= range.first && x2 >= range.second) ||
                          (x1 == range.second + 1) || (x2 == range.first + 1);
    if (overlapOrTouch) {
      range.first = min(range.first, x1);
      range.second = max(range.second, x2);
      return;
    }
  }
  ranges.emplace(x1, x2);
};

// Compute a set of ranges that represent impossible X coord given y
void getImpossibleXs(const info_t &info, set<range_t> &ranges, int y,
                     bool applyLimits = false) {
  for (const auto &[sensor, beacon] : info) {
    auto distance = direction::getManhattanDistBetween(sensor, beacon);
    int y_diff = abs(sensor.second - y);
    if (y_diff >= distance) {
      continue;
    }
    int x_diff = abs(distance - y_diff);
    int x1 = sensor.first - x_diff;
    int x2 = sensor.first + x_diff;
    if (applyLimits) {
      // for part 2
      x1 = max(x1, 0);
      x2 = min(x2, beaconLimit);
    }
    checkAndInsert(ranges, x1, x2);
  }
  return;
}

// return total number of positions represented by ranges
int getSum(const set<range_t> &ranges) {
  int total = 0;
  for (auto &range : ranges) {
    total += range.second - range.first + 1;
  }
  return total;
}

int part1(const info_t &info) {
  set<point_t> beaconsOnY;
  set<range_t> ranges;

  getImpossibleXs(info, ranges, rowY);
  for (const auto &[sensor, beacon] : info) {
    if (beacon.second == rowY) {
      beaconsOnY.emplace(beacon);
    }
  }
  return getSum(mergeTouchingRanges(ranges)) - beaconsOnY.size();
}

int findBeacon(const set<range_t> &ranges) {
  auto &first = *ranges.begin();
  auto &last = *ranges.rbegin();
  assert(last.first - first.second == 2);
  return first.second + 1;
}

string part2(const info_t &info) {
  point_t distressBeacon;
  for (int y = 0; y <= beaconLimit; ++y) {
    set<range_t> ranges;
    getImpossibleXs(info, ranges, y, true);
    ranges = mergeTouchingRanges(ranges);
    if (ranges.size() > 1) {
      auto x = findBeacon(ranges);
      distressBeacon = {x, y};
      break;
    }
  }
  UserPrint::print(' ', "distressBeacon pos", distressBeacon.first,
                   distressBeacon.second);
  int rem = distressBeacon.second / int(1e6);
  return to_string(distressBeacon.first * 4 + rem) +
         to_string(distressBeacon.second % int(1e6));
}

int main() {
  lines_t lines = getInput(INPUT);
  auto info = parseInput(lines);

  auto res1 = part1(info);
  UserPrint::print(' ', "Part 1 Result:", res1);

  auto res2 = part2(info);
  UserPrint::print(' ', "Part 2 Result:", res2);
}