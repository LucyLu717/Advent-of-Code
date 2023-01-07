#include "utils/algo.hpp"
#include "utils/input.hpp"
#include <set>

using namespace std;
using point_t = direction::coord_t;

const string INPUT = "../inputs/day9.txt";

struct Knot {
  point_t point;
  unique_ptr<Knot> next = nullptr;
};

// debug
void print(const point_t &head, const point_t &tail) {
  cout << "head " << head.first << " " << head.second << '\n';
  cout << "tail " << tail.first << " " << tail.second << '\n';
}

void move_tail(const point_t &head, point_t &tail) {
  int x_diff = head.first - tail.first;
  int y_diff = head.second - tail.second;
  if (x_diff == 0 && abs(y_diff) > 1) {
    // same row, head is up or down from tail
    tail.second += y_diff / abs(y_diff);
  } else if (y_diff == 0 && abs(x_diff) > 1) {
    // same column, head is left or right to tail
    tail.first += x_diff / abs(x_diff);
  } else if (abs(x_diff) > 1 || abs(y_diff) > 1) {
    // tail moves diagonally to keep up
    tail.first += x_diff / abs(x_diff);
    tail.second += y_diff / abs(y_diff);
  }
}

unique_ptr<Knot> create_knot_chain(int numKnots) {
  auto head = make_unique<Knot>();
  head->point = {0, 0};
  auto *curr = head.get();
  while (numKnots-- > 1) {
    auto next = make_unique<Knot>();
    next->point = {0, 0};
    curr->next = std::move(next);
    curr = curr->next.get();
  }
  return head;
}

int solution(const lines_t &lines, int numKnots) {
  auto head = create_knot_chain(numKnots);
  set<point_t> tailPos;
  for (const auto &line : lines) {
    auto [dir, step] = str::split(line, " ");
    auto d = static_cast<direction::Direction>(dir[0]);
    auto s = stol(step);
    while (s-- > 0) {
      switch (d) {
      case direction::Direction::eUp:
        direction::move_point<direction::Direction::eUp>(head.get()->point, 1);
        break;
      case direction::Direction::eDown:
        direction::move_point<direction::Direction::eDown>(head.get()->point,
                                                           1);
        break;
      case direction::Direction::eLeft:
        direction::move_point<direction::Direction::eLeft>(head.get()->point,
                                                           1);
        break;
      case direction::Direction::eRight:
        direction::move_point<direction::Direction::eRight>(head.get()->point,
                                                            1);
        break;
      }
      auto *curr = head.get();
      while (curr->next.get()) {
        move_tail(curr->point, curr->next->point);
        curr = curr->next.get();
      }
      tailPos.insert(curr->point);
    }
  }
  return tailPos.size();
}

int main() {
  lines_t lines = getInput(INPUT);
  auto res1 = solution(lines, 2);
  cout << "Part 1 Result: " << res1 << endl;
  auto res2 = solution(lines, 10);
  cout << "Part 2 Result: " << res2 << endl;
}
