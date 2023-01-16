#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

using point_t = direction::coord_t;
using abyss_edge_t = tuple<int, int, int>;

const string INPUT = "../inputs/day14.txt";
constexpr char ROCK = '#';
constexpr char SAND = 'o';
constexpr char AIR = '.';
const point_t SAND_START = {500, 0};

// return a set of all rocks
set<point_t> parseRocks(const lines_t &lines) {
  set<point_t> res;
  for (const auto &line : lines) {
    auto coords = str::splitAll(line, " -> ");
    vector<point_t> path(coords.size());
    transform(coords.begin(), coords.end(), path.begin(),
              [](const string &pair) -> point_t {
                auto tmp = str::split(pair, ',');
                return {stol(tmp.first), stol(tmp.second)};
              });
    for (int idx = 0; idx < path.size() - 1; ++idx) {
      res.insert(path[idx]);
      auto between = direction::get_points_between(path[idx], path[idx + 1]);
      res.insert(between.begin(), between.end());
    }
    res.insert(path.back());
  }
  return res;
}

// return positions representing rock edges
abyss_edge_t findAbyss(const set<point_t> &rocks) {
  auto bottom = 0;
  for (auto i = rocks.begin(); i != rocks.end(); ++i) {
    bottom = i->second > bottom ? i->second : bottom;
  }
  return {rocks.begin()->first /*left edge*/,
          rocks.rbegin()->first /*right edge*/, bottom};
}

// return whether the sand satisfies the condition
// p1: in the abyss
// p2: on the ground
template <typename Func>
bool sandFall(const set<point_t> &rocks, set<point_t> &sands, Func &&check,
              point_t &finalPos) {
  auto proceed = [&rocks, &sands](const point_t &pos) {
    // sand moves down, in face it's right direction by our representation
    point_t down = direction::move_point<direction::Direction::eUp>(pos, 1);
    point_t downLeft = direction::move_point<direction::Direction::eUp>(pos, 1);
    direction::move_point<direction::Direction::eLeft>(downLeft, 1);
    point_t downRight =
        direction::move_point<direction::Direction::eUp>(pos, 1);
    direction::move_point<direction::Direction::eRight>(downRight, 1);
    if (rocks.find(down) == rocks.end() && sands.find(down) == sands.end()) {
      return down;
    }
    if (rocks.find(downLeft) == rocks.end() &&
        sands.find(downLeft) == sands.end()) {
      return downLeft;
    }
    if (rocks.find(downRight) == rocks.end() &&
        sands.find(downRight) == sands.end()) {
      return downRight;
    }
    return pos;
  };
  point_t pos = SAND_START;
  auto abyssEdges = findAbyss(rocks);

  while (!check(pos, abyssEdges)) {
    auto newPos = proceed(pos);
    if (newPos == pos) {
      // can't proceed, rest here
      sands.insert(newPos);
      finalPos = newPos;
      return false;
    }
    pos = newPos;
  }
  finalPos = pos;
  return true;
}

int part1(const set<point_t> &rocks) {
  set<point_t> sands;
  int count = 0;
  auto inAbyss = [](const point_t &pos, const abyss_edge_t &abyssEdges) {
    if (pos.first < get<0>(abyssEdges) || pos.first > get<1>(abyssEdges) ||
        pos.second > get<2>(abyssEdges)) {
      return true;
    }
    return false;
  };
  point_t pos;
  while (!sandFall(rocks, sands, inAbyss, pos)) {
    ++count;
  }
  return count;
}

int part2(const set<point_t> &rocks) {
  set<point_t> sands;
  int count = 0;
  auto onTheGround = [](const point_t &pos, const abyss_edge_t &abyssEdges) {
    if (pos.second == get<2>(abyssEdges) + 1) {
      return true;
    }
    return false;
  };
  point_t pos;
  while (true) {
    sandFall(rocks, sands, onTheGround, pos);
    sands.insert(pos);
    ++count;
    if (pos == SAND_START) {
      return count;
    }
  }
}

int main() {
  lines_t lines = getInput(INPUT);
  auto rocks = parseRocks(lines);

  auto res1 = part1(rocks);
  UserPrint::print(' ', "Part 1 Result:", res1);

  auto res2 = part2(rocks);
  UserPrint::print(' ', "Part 2 Result:", res2);
}