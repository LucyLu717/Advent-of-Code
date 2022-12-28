#include <algorithm>

#include "utils/input.hpp"
#include "utils/algo.hpp"

using namespace std;

const string INPUT = "../inputs/day3.txt";

constexpr int a_value = 1;
constexpr int A_value = 27;

int letter_score(char letter) {
    int score = 0;
    if( isupper(letter) ) {
        score = A_value + letter - 'A';
    } else {
        score = a_value + letter - 'a';
    }
    return score;
}

int part1(const vector<string>& rucksacks) {
    int score = 0;
    for( const auto& sack: rucksacks) {
        auto firstSet = convertStrToSet(sack.substr(0, sack.size() / 2));
        auto secondSet = convertStrToSet(sack.substr(sack.size() / 2 , sack.size() / 2));
        vector<char> overlap;
        set_intersection(firstSet.begin(), firstSet.end(), secondSet.begin(), secondSet.end(),
                         back_inserter(overlap));
        assert(overlap.size() == 1);
        score += letter_score(overlap[0]);
    }
    return score;
}

int part2(const vector<string>& rucksacks) {
    int score = 0;
    assert(rucksacks.size() % 3 == 0);
    auto it = rucksacks.begin();
    while( it < rucksacks.end() ) {
        auto firstElf = convertStrToSet(*it++);
        auto secondElf = convertStrToSet(*it++);
        auto thirdElf = convertStrToSet(*it++);
        vector<char> overlap1;
        set_intersection(firstElf.begin(), firstElf.end(), secondElf.begin(), secondElf.end(),
                         back_inserter(overlap1));
        vector<char> overlap;
        set_intersection(thirdElf.begin(), thirdElf.end(), overlap1.begin(), overlap1.end(),
                         back_inserter(overlap));
        assert(overlap.size() == 1);
        score += letter_score(overlap[0]);
    }
    return score;
}

int main() {
    vector<string> rucksacks = getInput(INPUT);

    // part 1
    int score1 = part1(rucksacks);
    cout << "Part 1 Score: " << score1 << endl;

    // part 2
    int score2 = part2(rucksacks);
    cout << "Part 2 Score: " << score2 << endl;
}
