def read_input():
    with open("../inputs/day9.txt") as f:
        numbers = [int(line.strip()) for line in f]
    return numbers


# Overengineered solution...

# def get_sums(numbers):
#     sums = {}
#     results = {}
#     for i in range(len(numbers)):
#         sums[numbers[i]] = {}
#         for j in range(i + 1, len(numbers)):
#             sums[numbers[i]][numbers[j]] = numbers[i] + numbers[j]
#             num = results.get(sums[numbers[i]][numbers[j]], 0)
#             results[sums[numbers[i]][numbers[j]]] = num + 1
#     return sums, results


# def update_sums(sums, results, n, prev):
#     for _, j in sums[prev].items():
#         results[j] -= 1
#     sums.pop(prev)
#     sums[n] = {}
#     for k in sums.keys():
#         if k != n:
#             sums[k][n] = k + n
#             num = results.get(sums[k][n], 0)
#             results[sums[k][n]] = num + 1
#     return sums, results


# def find_anormaly(numbers, plength=25):
#     sums, results = get_sums(numbers[:plength])
#     for k in range(plength, len(numbers)):
#         n = numbers[k]
#         if n not in results or results[n] == 0:
#             print(n)
#             return n
#         else:
#             sums, results = update_sums(sums, results, n, numbers[k - plength])


def two_sum(numbers, n):
    num_set = set(numbers)
    for num in numbers:
        num_set.remove(num)
        if n - num in num_set:
            return True
        num_set.add(num)
    return False


def find_anormaly(numbers, plength=25):
    for k in range(plength, len(numbers)):
        n = numbers[k]
        normal = two_sum(numbers[k - plength : k], n)
        if not normal:
            print(n)
            return n


def find_set(numbers, n):
    start = 0
    end = 0
    _sum = numbers[0]
    while _sum != n and end < len(numbers) - 1:
        end += 1
        last = numbers[end]
        while last + _sum > n:
            first = numbers[start]
            _sum = _sum - first
            start += 1
        _sum += last
    numbers_set = numbers[start : end + 1]
    print(min(numbers_set) + max(numbers_set))


def main():
    numbers = read_input()
    n = find_anormaly(numbers)
    find_set(numbers, n)


if __name__ == "__main__":
    main()
