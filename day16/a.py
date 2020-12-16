def read_input():
    with open("../inputs/day16.txt") as f:
        info = [section.split("\n") for section in f.read().split("\n\n")]
    return info


import numpy as np
from itertools import permutations
from collections import defaultdict
import copy


def ranges(fields):
    fields_dict = {}
    for f in fields:
        fields_dict[f.split(":")[0]] = (
            f.split(":")[1].strip().replace("or ", "").split(" ")
        )
    numbers = set()
    for name, f in fields_dict.items():
        new_list = []
        for r in f:
            ran = r.split("-")
            numbers.update(range(int(ran[0]), int(ran[1]) + 1))
            new_list.append((int(ran[0]), int(ran[1])))
        fields_dict[name] = new_list
    return numbers, fields_dict


def rate(nearby, numbers):
    rate = 0
    valid_tickets = []
    for l in nearby[1:]:
        not_valid = False
        nums = l.split(",")
        int_nums = []
        for n in nums:
            n = int(n)
            if n not in numbers:
                rate += n
                not_valid = True
            int_nums.append(n)
        if not not_valid:
            nums = np.array(nums)
            valid_tickets.append(int_nums)
    print(rate)
    return np.array(valid_tickets)


# Too slow:
# def check(perm, doesntwork):
#     for ind, field in enumerate(perm):
#         if ind in doesntwork[field]:
#             return False
#     return True

# def find(fields_dict, valid_tickets):
#     perms = permutations(fields_dict.keys())
#     doesntwork = defaultdict(list)
#     counter = 0
#     for perm in perms:
#         counter += 1
#         if counter % 10000000 == 0:
#             print(counter, perm)
#         perm = list(perm)
#         if not check(perm, doesntwork):
#             continue
#         perm_working = True
#         for col, name in enumerate(perm):
#             col_work = True
#             ranges = fields_dict[name]
#             column = valid_tickets[:, col]
#             for num in column:
#                 working = False
#                 for ran in ranges:
#                     working = working or ran[0] <= num <= ran[1]
#                     if working:
#                         break
#                 col_work = col_work and working
#                 if not col_work:
#                     doesntwork[name].append(col)
#                     break
#             perm_working = perm_working and col_work
#             if not perm_working:
#                 break
#         if perm_working:
#             print(perm)
#             return perm


def find(fields_dict, valid_tickets):
    col_to_field = defaultdict(list)
    for name, ranges in fields_dict.items():
        for col in range(len(valid_tickets[0, :])):
            col_work = True
            column = valid_tickets[:, col]
            for num in column:
                working = False
                for ran in ranges:
                    working = working or ran[0] <= num <= ran[1]
                    if working:
                        break
                col_work = col_work and working
                if not col_work:
                    break
            if col_work:
                col_to_field[col].append(name)
    perm = {}
    allf = list(fields_dict.keys())
    while len(perm) != len(allf):
        column = 0
        while column < len(allf):
            f = col_to_field[column]  # list of fields applicable to a column
            if len(f) == 1:
                a = f[0]
                perm[column] = a
                for col, fields in col_to_field.items():
                    if len(fields) > 0:
                        fields.remove(a)
                column = 0
            else:
                column += 1
    return perm


inputs = read_input()
valid_nums, fields_dict = ranges(inputs[0])
valid_tickets = rate(inputs[2], valid_nums)
perm = find(fields_dict, valid_tickets)

# Find product
my_ticket = [int(i) for i in inputs[1][1].split(",")]
res = 1
for col, f in perm.items():
    if "departure" in f:
        res *= my_ticket[col]
print(res)
