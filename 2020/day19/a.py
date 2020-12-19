def read_input():
    with open("../inputs/day19.txt") as f:
        info = f.read().split("\n\n")
        rules = info[0].split("\n")
        msges = info[1].split("\n")

        rules_t = {}
        for l in rules:
            r = l.split(":")
            key = r[0].strip()
            content = [nums.strip() for nums in r[1].strip().split("|")]
            rules_t[key] = content
    return rules_t, msges


# Over engineered
# def search(rules, msg, rnum, leaves):
#     rule = rules[rnum]
#     if '"' in rule[0]:
#         if msg[leaves] == rule[0][1:-1]:
#             if rnum == "0" and leaves < len(msg):
#                 return False, leaves
#             return True, leaves + 1
#         else:
#             return False, leaves
#     for subrule in rule:
#         nums = subrule.split(" ")
#         subrule_valid = True
#         new_leaves = leaves
#         for n in nums:
#             valid, new_leaves = search(rules, msg, n, new_leaves)
#             # print(msg, rnum, leaves, new_leaves, subrule, n, valid)
#             if not valid:
#                 subrule_valid = False
#                 break
#         if subrule_valid:
#             leaves = new_leaves
#             if rnum == "0" and leaves < len(msg):
#                 return False, leaves
#             return True, leaves
#     return False, leaves


def find(rules, rnum):
    rule = rules[rnum]
    if '"' in rule[0]:
        results = [rule[0][1:-1]]
        return results
    results = []
    for subrule in rule:
        nums = subrule.split(" ")
        new_results = []
        for n in nums:
            n_results = find(rules, n)
            if new_results == []:
                new_results = n_results
            else:
                new_results = [r + i for r in new_results for i in n_results]
        results.extend(new_results)
    return results


def simplified(msg, keys):
    return msg in keys["0"]


def valid_eleven(msg, keys):
    len42 = len(keys["42"][0])
    len31 = len(keys["31"][0])
    if len(msg) == 0 or len(msg) % (len42 + len31) != 0:
        return False
    lim = len(msg) // (len42 + len31)
    for i in range(1, lim + 1):
        if msg[len42 * (i - 1) : len42 * i] not in keys["42"]:
            return False
        if i == 1:
            if msg[-len31 * i :] not in keys["31"]:
                return False
        elif msg[-len31 * i : -len31 * (i - 1)] not in keys["31"]:
            return False
    return True


def loops(msg, keys):
    length = len(keys["42"][0])
    lim = len(msg) // length
    for i in range(1, lim + 1):
        if not msg[length * (i - 1) : length * i] in keys["42"]:
            return False
        if valid_eleven(msg[length * i :], keys):
            return True
    return False


def search_all(msges, keys, f):
    # valid_msges = [m for m in msges if search(rules, m, "0", 0)[0]]
    # print(len(valid_msges))
    valid_msges = [m for m in msges if f(m, keys)]
    print(len(valid_msges))


rules, msges = read_input()
keys = {r: find(rules, r) for r in rules.keys()}
search_all(msges, keys, simplified)
search_all(msges, keys, loops)
