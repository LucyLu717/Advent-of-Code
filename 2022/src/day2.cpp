#include <algorithm>
#include <map>
#include <numeric>
#include <set>

#include "utils/input.hpp"

using namespace std;

const string INPUT = "../inputs/day2.txt";
const set<string> WIN = {"A Y", "B Z", "C X"};
const set<string> TIE = {"A X", "B Y", "C Z"};
const set<string> LOSE = {"B X", "C Y", "A Z"};
constexpr int WIN_SCORE = 6;
constexpr int TIE_SCORE = 3;
constexpr int LOSE_SCORE = 0;

const map<char, int> SHAPE_SCORE = {{'X', 1}, {'Y', 2}, {'Z', 3}};

const map<string, string> INSTR_TO_ROUND = {{"A X", "A Z"}, {"A Y", "A X"}, {"A Z", "A Y"}, {"B X", "B X"}, {"B Y", "B Y"}, {"B Z", "B Z"}, {"C X", "C Y"}, {"C Y", "C Z"}, {"C Z", "C X"}};

int get_round_score(string round) {
    int score = SHAPE_SCORE.at(round.back());
    if (WIN.find(round) != WIN.end()) {
        score += WIN_SCORE;
    } else if (TIE.find(round) != TIE.end()) {
        score += TIE_SCORE;
    } else if (LOSE.find(round) != LOSE.end()) {
        score += LOSE_SCORE;
    } else {
        throw runtime_error("round invalid: " + round);
    }
    return score;
}

int get_total(const vector<string>& game, vector<int>& result) {
    transform(game.cbegin(), game.cend(), result.begin(), get_round_score);
    return accumulate(result.begin(), result.end(), 0);
}

string get_new_round(string instruction) {
    return INSTR_TO_ROUND.at(instruction);
}

int get_new_total(vector<string>& game, vector<int>& result) {
    transform(game.cbegin(), game.cend(), game.begin(), get_new_round);
    transform(game.cbegin(), game.cend(), result.begin(), get_round_score);
    return accumulate(result.begin(), result.end(), 0);
}

int main() {
    vector<string> game = getInput(INPUT);
    vector<int> result;
    for( const auto& elt: game) { result.push_back(0); }

    // part 1
    auto total = get_total(game, result);
    cout << "total: " << total << endl;

    // part 2
    auto new_total = get_new_total(game, result);
    cout << "new total: " << new_total << endl;
}
