def main():
    with open("input.txt") as file:
        name_tree = dict()

        row = file.readline().rstrip()

        while row is not None and len(row) > 0:
            orbits = row.split(")")
            center = orbits[0]
            orbit = orbits[1]
            if center not in name_tree.keys():
                name_tree[center] = Tree(center)
            if orbit not in name_tree.keys():
                name_tree[orbit] = Tree(orbit)
            name_tree[center].add_child(name_tree[orbit])
            row = file.readline().rstrip()

        root_tree = name_tree["COM"]
        part_one(root_tree)
        part_two(root_tree)


def part_one(root_tree):
    root_tree.sum_children()
    sum = dfs(root_tree, set())
    print(sum)


def part_two(root_tree):
    list_you = find(root_tree, "YOU")
    list_you.remove("YOU")

    list_san = find(root_tree, "SAN")
    list_san.remove("SAN")

    short = list_you if len(list_you) < len(list_san) else list_san
    long = list_san if short == list_you else list_you
    ptr_short = 0
    ptr_long = 0

    while ptr_short < len(short) and short[ptr_short] == long[ptr_long]:
        ptr_short += 1
        ptr_long += 1

    print(len(short) - ptr_short + len(long) - ptr_long)


def dfs(root_tree, visited):
    if root_tree.children is None:
        return 0
    visited.add(root_tree.root)
    sum = root_tree.total_children
    for child in root_tree.children:
        if child.root not in visited:
            sum += dfs(child, visited)
    return sum


def find(root_tree, name):
    if root_tree.root == name:
        return [name]
    parents = []
    for child in root_tree.children:
        parents = find(child, name)
        if len(parents) != 0:
            parents.insert(0, root_tree.root)
            return parents
    return parents


class Tree:

    def __init__(self, root):
        self.root = root
        self.children = []
        self.total_children = -1

    def add_child(self, child):
        self.children.append(child)

    def sum_children(self):
        sum = len(self.children)
        for child in self.children:
            sum += child.sum_children()
        self.total_children = sum
        return sum


if __name__ == '__main__':
    main()