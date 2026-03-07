import os

BASE_DIR = "generated_values_sp"

# Range automático
MIN_sp = 30
MAX_sp = 4320
STEP = 15

# Dispositivos reais
EXTRA_SIZES = [
    384, 393, 400, 410, 411, 412,
    427, 432, 440, 533,
    640, 667, 673, 685,
    768, 820,
    1024, 1280
]

# Dimens
MAX_POSITIVE = 600
MAX_NEGATIVE = 0
BASE_sp = 300.0

QUALIFIERS = ["sw", "w", "h"]

SUFFIX = {
    "sw": "ssp",
    "w": "wsp",
    "h": "hsp"
}


def generate_xml(scale, q):
    lines = ['<?xml version="1.0" encoding="utf-8"?>', '<resources>']

    suffix = SUFFIX[q]

    for i in range(1, MAX_POSITIVE + 1):
        val = i * scale
        lines.append(f'<dimen name="_{i}{suffix}">{val:.2f}sp</dimen>')

    for i in range(1, MAX_NEGATIVE + 1):
        val = -i * scale
        lines.append(f'<dimen name="_minus{i}{suffix}">{val:.2f}sp</dimen>')

    lines.append('</resources>')
    return "\n".join(lines)


def build_size_list():
    auto_sizes = list(range(MIN_sp, MAX_sp + 1, STEP))
    all_sizes = set(auto_sizes + EXTRA_SIZES)
    return sorted(all_sizes)


def generate():
    sizes = build_size_list()

    os.makedirs(BASE_DIR, exist_ok=True)

    # values base
    values_dir = os.path.join(BASE_DIR, "values")
    os.makedirs(values_dir, exist_ok=True)

    for q in QUALIFIERS:
        xml = generate_xml(1.0, q)

        with open(os.path.join(values_dir, f"{q}sps.xml"), "w", encoding="utf-8") as f:
            f.write(xml)

    # qualifiers específicos
    for size in sizes:

        scale = size / BASE_sp

        for q in QUALIFIERS:

            folder = os.path.join(BASE_DIR, f"values-{q}{size}dp")
            os.makedirs(folder, exist_ok=True)

            xml = generate_xml(scale, q)

            with open(os.path.join(folder, f"{q}sps.xml"), "w", encoding="utf-8") as f:
                f.write(xml)


if __name__ == "__main__":
    generate()
    print("Dimensions generated successfully!")
