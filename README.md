# HBM's Nuclear Tech Mod: Resurrection (1.21.1)

A modern port of the legendary **HBM's Nuclear Tech Mod** from Minecraft 1.7.10 to **1.21.1**!

This project aims to bring back the iconic advanced technology, nuclear weaponry, hazardous mechanics, and industrial automation to modern Minecraft, completely rewritten and adapted for newer engine standards.

---

> [!WARNING]
> **EXPERIMENTAL / PRE-ALPHA BUILD**
> A significant amount of content is still missing or under active porting. This mod is **NOT ready for survival play**!
> * Expect bugs, crashes, and visual glitches.
> * **Do not** use this mod on worlds you care about.
> * World resets may be required between updates.
> 
> 
> Please report any issues or crashes on our [GitHub Issues](https://www.google.com/search?q=../../issues) page.

---

## ⚡ Features & Port Progress

* ☢️ **Radiation & Hazards System:** Chunk-based radiation spread, radioactive contamination, and hazardous materials (pyrophoric, explosive, toxic).
* ⚙️ **Modernized Machinery:** Energy generation, multiblock structures, and automated processing pipelines.
* 🛠️ **Equipment & Armors:** Specialized radiation gear, advanced tools, and weaponry.
* 🔄 **Code Base Overhaul:** Rewritten mechanics to align with NeoForge standards on 1.21.1.

---

## 📦 Installation

### Requirements

* **Minecraft:** `1.21.1`
* **Mod Loader:** **NeoForge** *(Recommended version: 21.1.x)*

### Steps

1. Download the latest `.jar` release from the [Releases](https://www.google.com/search?q=..%2F..%2Freleases) tab.
2. Install NeoForge for Minecraft 1.21.1.
3. Drop the downloaded `.jar` file into your `.minecraft/mods` folder.
4. Launch the game.

---

## ⚠️ Known Issues & Compatibility

* **Survival Mode:** Incomplete recipe trees and missing mechanics make survival gameplay currently unviable.
* **Shaders / Iris:** Rendering issues or custom pipeline crashes may occur when using shader mods.
* **World Generation:** Custom ores and structures are in active development.

---

## 🛠️ Development & Building

If you want to compile the project locally or contribute to development:

### Prerequisites

* **JDK 21** (Required for Minecraft 1.21.1 development)

### Quickstart

1. **Clone the repository:**
```bash
git clone https://github.com/votre-pseudo/votre-repo.git
cd votre-repo

```


2. **Setup workspace & build:**
* **Windows:**
```cmd
.\gradlew.bat build

```


* **Linux / macOS:**
```bash
./gradlew build

```




3. **Run the client in IDE:**
```bash
./gradlew runClient

```


The compiled `.jar` file will be generated in `build/libs/`.

---

## 🤝 Contributing

Contributions are warmly welcome! Whether it is bug fixes, porting specific blocks/items, or improving performance:

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/amazing-feature`).
3. Commit your changes (`git commit -m 'Add amazing feature'`).
4. Push to the branch (`git push origin feature/amazing-feature`).
5. Open a **Pull Request**.

---

## 📜 Credits & License

* **Original Mod:** Created by **The Bobcat** (*HbmMods*).
* **NeoForged Team & Mojang:** For the modding API and Minecraft.
* **Community Contributors:** Thanks to all past forks (1.12.2 / 1.20.1 / 1.21.1) for inspiration and reference implementations.

This project is licensed under the **GNU Lesser General Public License v3.0** (LGPL-3.0). See the [LICENSE](https://www.google.com/search?q=LICENSE) file for details.
