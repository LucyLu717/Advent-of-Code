#include <deque>
#include <sstream>

#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day13.txt";

class Element {
public:
  Element(bool isList, int val = -1) : isList_(isList), val_(val){};

  bool isInt() const { return !isList_; }
  bool isList() const { return isList_; }

  int getValue() const { return val_; }
  void setValue(int val) { val_ = val; }
  Element &addElement(const Element &elm) {
    elms.push_back(elm);
    return *this;
  }
  const vector<Element> &getElements() const { return elms; }

  // returns neg if this < rhs, 0 if this == rhs, pos if this > rhs
  int compare(const Element &rhs) const {
    if (isInt() && rhs.isInt()) {
      return val_ - rhs.val_;
    }
    if (isList() && rhs.isList()) {
      int size = elms.size() < rhs.elms.size() ? elms.size() : rhs.elms.size();
      for (int idx = 0; idx < size; ++idx) {
        Element left = elms[idx];
        Element right = rhs.elms[idx];
        int val = left.compare(right);
        if (val != 0) {
          return val;
        }
      }
      return elms.size() - rhs.elms.size();
    }
    if (isInt()) {
      Element tmp{true};
      tmp.addElement(*this);
      return tmp.compare(rhs);
    } else {
      Element tmp{true};
      tmp.addElement(rhs);
      return compare(tmp);
    }
  }

  friend ostream &operator<<(ostream &os, const Element &elm) {
    if (elm.isInt()) {
      os << elm.getValue();
      return os;
    }

    if (elm.isList()) {
      os << '[';
    }
    const auto &elms = elm.getElements();
    for (int idx = 0; idx < elms.size(); ++idx) {
      os << elms[idx];
      if (idx != elms.size() - 1) {
        os << ',';
      }
    }
    if (elm.isList()) {
      os << ']';
    }
    return os;
  }

private:
  bool isList_ = false;
  // for int
  int val_ = -1;
  // for list
  vector<Element> elms;
};

Element parsePacket(const string &line) {
  deque<Element> stack;
  Element curr(true);
  int currVal = -1;

  auto updateIntVal = [&curr, &currVal]() {
    curr.setValue(currVal);
    currVal = -1;
  };

  auto updateLast = [&stack, &curr]() {
    auto &last = stack.back();
    last.addElement(curr);
    curr = last;
    stack.pop_back();
  };

  // ignore start/end brackets since every packet is a list
  for (int idx = 1; idx < line.size() - 1; ++idx) {
    char c = line[idx];
    if (c == '[') {
      stack.push_back(curr);
      curr = Element(true);
    } else if (c == ']') {
      if (curr.isInt()) {
        updateIntVal();
        // extra update for Ints
        // because they need to be added to the list before completing the list
        updateLast();
      }
      updateLast();
    } else if (c == ',') {
      if (curr.isInt()) {
        updateIntVal();
        // update for Ints
        // because they need to be added to the parent list
        updateLast();
      }
    } else {
      if (currVal < 0) {
        stack.push_back(curr);
        curr = Element(false);
        currVal = 0;
      }
      currVal = currVal * 10 + c - '0';
    }
  }
  // handle outer most list layer
  // exclude special case []
  if (stack.size()) {
    if (curr.isInt()) {
      updateIntVal();
    }
    updateLast();
  }
  return curr;
}

int part1(const lines_t &lines) {
  int counter = 1;
  int result = 0;
  vector<Element> pair;
  for (const auto &line : lines) {
    if (line.size()) {
      Element packet = parsePacket(line);
      ostringstream os{};
      os << packet;
      assert(os.str() == line);
      pair.push_back(packet);
    } else {
      if (pair[0].compare(pair[1]) <= 0) {
        result += counter;
      }
      ++counter;
      pair.clear();
    }
  }
  return result;
}

template <int Val> Element createDriver() {
  return Element(true).addElement(
      Element(true).addElement(Element(false, Val)));
}

int part2(const lines_t &lines) {
  Element driverTwo = createDriver<2>();
  Element driverSix = createDriver<6>();
  vector<Element> allPkts = {driverTwo, driverSix}; // indices 0 and 1

  for (const auto &line : lines) {
    if (line.size()) {
      Element packet = parsePacket(line);
      ostringstream os{};
      os << packet;
      assert(os.str() == line);
      allPkts.push_back(packet);
    }
  }

  // sort indices
  vector<int> indices(allPkts.size());
  iota(indices.begin(), indices.end(), 0);
  sort(indices.begin(), indices.end(),
       [&allPkts](int a, int b) { return allPkts[a].compare(allPkts[b]) < 0; });

  // index is 1-based
  int two = find(indices.begin(), indices.end(), 0) - indices.begin() + 1;
  int six = find(indices.begin(), indices.end(), 1) - indices.begin() + 1;

  return two * six;
}

int main() {
  lines_t lines = getInput(INPUT);
  // append empty line
  lines.emplace_back();
  assert(lines.size() % 3 == 0);

  auto res1 = part1(lines);
  UserPrint::print(' ', "Part 1 Result:", res1);

  auto res2 = part2(lines);
  UserPrint::print(' ', "Part 2 Result:", res2);
}
