import math


def read_input():
    reactions = dict()
    with open("input.txt") as file:
        reaction = file.readline().rstrip()
        while reaction is not None and len(reaction) > 0:
            materials, product = map(lambda x: x.strip(), reaction.split("=>"))
            materials_in_dict = []
            for material in materials.split(","):
                material = material.strip()
                amt, name = material.split(" ")
                materials_in_dict.append((name, amt))
            product_amt, product_name = product.split(" ")
            reactions[product_name] = (product_amt, materials_in_dict)
            reaction = file.readline().strip()
    return reactions


def part_one(reactions):
    materials = dict()
    residuals = dict()
    materials["FUEL"] = 1
    while len(materials) > 1 or "ORE" not in materials.keys():
        new_materials = dict()
        for product_name in materials.keys():
            product_amt = materials[product_name]
            if product_name != "ORE":
                product_formula_amt, formula_materials = reactions.get(product_name)
                residual = residuals[product_name] if product_name in residuals.keys() else 0
                product_amt = product_amt - residual if product_amt - residual >= 0 else 0
                remaining = residual - product_amt if product_amt - residual <= 0 else 0

                for material_name, material_amt in formula_materials:

                    amount = int(material_amt) * math.ceil(product_amt / int(product_formula_amt))
                    overflow = int(product_formula_amt) * math.ceil(product_amt / int(product_formula_amt)) - product_amt
                    residuals[product_name] = remaining + overflow

                    new_materials[material_name] = \
                        (new_materials[material_name] if material_name in new_materials.keys() else 0) + amount
                    residuals.setdefault(material_name, 0)
            else:
                new_materials[product_name] = \
                    (new_materials[material_name] if material_name in new_materials.keys() else 0) + product_amt
        materials = new_materials
    print(materials)


def part_two(reactions):
    for i in range(2260000, 2270000):
        materials = dict()
        residuals = dict()
        materials["FUEL"] = i
        while len(materials) > 1 or "ORE" not in materials.keys():
            new_materials = dict()
            for product_name in materials.keys():
                product_amt = materials[product_name]
                if product_name != "ORE":
                    product_formula_amt, formula_materials = reactions.get(product_name)
                    residual = residuals[product_name] if product_name in residuals.keys() else 0
                    product_amt = product_amt - residual if product_amt - residual >= 0 else 0
                    remaining = residual - product_amt if product_amt - residual <= 0 else 0

                    for material_name, material_amt in formula_materials:
                        amount = int(material_amt) * math.ceil(product_amt / int(product_formula_amt))
                        overflow = int(product_formula_amt) * math.ceil(
                            product_amt / int(product_formula_amt)) - product_amt
                        residuals[product_name] = remaining + overflow

                        new_materials[material_name] = \
                            (new_materials[material_name] if material_name in new_materials.keys() else 0) + amount
                        residuals.setdefault(material_name, 0)
                else:
                    new_materials[product_name] = \
                        (new_materials[material_name] if material_name in new_materials.keys() else 0) + product_amt
            materials = new_materials
        if materials["ORE"] > 1000000000000:
            print(i - 1)
            break


def main():
    reactions = read_input()
    part_one(reactions)
    part_two(reactions)


if __name__ == '__main__':
    main()
