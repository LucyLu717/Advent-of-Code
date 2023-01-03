#include <set>
#include <string>

using namespace std;

// convert a string into a set of chars
set<char> convertStrToSet( const string& str ) {
    set<char> res;
    for( const char& c: str ) {
        res.insert(c);
    }
    return res;
}

// split a string around the first occurrence of a given character
pair<string, string> split(const string& str, char sep) {
    auto pos = str.find(sep);
    if( string::npos == pos ) {
        throw runtime_error("no sep " + string(1, sep) + " found in str " + str);
    }
    return make_pair(str.substr(0, pos), str.substr(pos+1));
}

// split a string around the first occurrence of a given string pattern
pair<string, string> split(const string& str, const string& sep) {
    auto pos = str.find(sep);
    if( string::npos == pos ) {
        throw runtime_error("no sep " + sep + " found in str " + str);
    }
    return make_pair(str.substr(0, pos), str.substr(pos+sep.size()));
}