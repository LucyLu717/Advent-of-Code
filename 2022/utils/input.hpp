#include <fstream>
#include <iostream>
#include <vector>
#include <string>

using namespace std;

vector<string> getInput( const string& filen ) {
    ifstream input(filen);
    if (!input.is_open()) {
        throw runtime_error("input invalid");
    }

    // process input
    string line;
    vector<string> result;
    while (getline(input, line)) {
        result.push_back(line);
    }
    return result;
}
