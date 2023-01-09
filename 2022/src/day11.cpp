#include "utils/algo.hpp"
#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day11.txt";
constexpr int MONKEY_LINES = 7;
constexpr int ROUND_1 = 20;
constexpr int ROUND_2 = 10000;
constexpr int WORRY_DIVISOR = 3;

struct Monkeys {
  int monkeyNum = 0;
  vector<vector<uint64_t>> monkeyItems;           // monkey -> list of items
  vector<function<uint64_t(uint64_t)>> monkeyOps; // monkey -> op
  vector<int> monkeyTestDiv;          // monkey -> test dividible by num
  vector<pair<int, int>> nextMonkeys; // monkey -> (true, false) monkey idx
};

Monkeys parse(const lines_t &lines) {
  int monkeyInd = 0;
  Monkeys data;

  auto parse_monkey = [&lines, &monkeyInd, &data]() {
    int counter = 1;
    while (counter < MONKEY_LINES) {
      const auto &line = lines[monkeyInd * MONKEY_LINES + counter];
      switch (counter) {
      case 1: {
        auto [_, commaList] = str::split(line, ": ");
        auto itemsS = str::splitAll(commaList, ", ");
        vector<uint64_t> items(itemsS.size(), 0);
        transform(itemsS.cbegin(), itemsS.cend(), items.begin(),
                  [](string s) { return stol(s); });
        data.monkeyItems.push_back(items);
        break;
      }
      case 2: {
        auto [_, opString] = str::split(line, " old ");
        auto [op, rhs] = str::split(opString, ' ');
        switch (op[0]) {
        case '+': {
          int val = stol(rhs);
          data.monkeyOps.push_back([val](uint64_t old) { return old + val; });
          break;
        }
        case '-': {
          int val = stol(rhs);
          data.monkeyOps.push_back([val](uint64_t old) { return old - val; });
          break;
        }
        case '*': {
          if (rhs == "old") {
            data.monkeyOps.push_back([](uint64_t old) { return old * old; });
          } else {
            int val = stol(rhs);
            data.monkeyOps.push_back([val](uint64_t old) { return old * val; });
          }
          break;
        }
        default:
          assert(false);
        }
        break;
      }
      case 3: {
        auto [_, numS] = str::split(line, " by ");
        data.monkeyTestDiv.push_back(stol(numS));
        break;
      }
      case 4: {
        auto [_, numS] = str::split(line, " monkey ");
        data.nextMonkeys.push_back(make_pair<int, int>(stol(numS), 0));
        break;
      }
      case 5: {
        auto [_, numS] = str::split(line, " monkey ");
        data.nextMonkeys[monkeyInd].second = stol(numS);
        break;
      }
      default:
        break;
      }
      ++counter;
    }
    ++monkeyInd;
  };

  while (monkeyInd * MONKEY_LINES < lines.size()) {
    parse_monkey();
  }
  data.monkeyNum = monkeyInd;

  return std::move(data);
}

template <typename Func>
vector<uint64_t> inspectItems(Monkeys data, int totalRound, Func &&func) {
  vector<uint64_t> inspections(data.monkeyNum, 0);
  for (int round = 0; round < totalRound; ++round) {
    for (int monkey = 0; monkey < data.monkeyNum; ++monkey) {
      for (auto item : data.monkeyItems[monkey]) {
        uint64_t res = func(data.monkeyOps[monkey](item));
        int targetMonkey = -1;
        if (res % data.monkeyTestDiv[monkey] == 0) {
          targetMonkey = data.nextMonkeys[monkey].first;
        } else {
          targetMonkey = data.nextMonkeys[monkey].second;
        }
        data.monkeyItems[targetMonkey].push_back(res);
      }
      inspections[monkey] += data.monkeyItems[monkey].size();
      data.monkeyItems[monkey].clear();
    }
  }
  return inspections;
}

uint64_t monkeyBusiness(vector<uint64_t> &inspections) {
  vec::sortVec(inspections, greater<uint64_t>());
  return inspections[0] * inspections[1];
}

int main() {
  lines_t lines = getInput(INPUT);
  Monkeys data = parse(lines);
  auto inspections = inspectItems(
      data, ROUND_1, [](uint64_t item) { return item / WORRY_DIVISOR; });
  auto res1 = monkeyBusiness(inspections);
  cout << "Part 1 Result: " << res1 << endl;
  auto primeProduct = vec::accu(data.monkeyTestDiv, 1, multiplies<int>());
  inspections = inspectItems(data, ROUND_2, [&primeProduct](uint64_t item) {
    return item % primeProduct;
  });
  auto res2 = monkeyBusiness(inspections);
  cout << "Part 2 Result: " << res2 << endl;
}
