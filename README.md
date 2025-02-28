# 🚀  Space Invader Game

Welcome to the **Space Invader Game** repository!  
This repository contains my Space Invader Game implementation.  

📌 This project is developed using **Java 8 (Oracle) and JavaFX**, focusing on GUI programming principles.

---

## 📂 Repository Structure  

📜 **src/** → Contains source code files for the game and the assets
📄 **Checklist.pdf** → Requirement checklist  
📄 **README.md** → Documentation for setup and execution    

---

## 🎮 Game Features  

- **Player-controlled spaceship** that moves **left** and **right**.  
- **Shoot bullets** to destroy **enemy ships**.  
- **Enemy ships** spawn every 2 seconds and move downward.  
- **Scoring System**:
  - Destroying an enemy ship grants **100 points**.  
  - Collecting a reward increases the score by **50 points**.  
  - Collecting a penalty decreases the score by **50 points**.  
- **Power-ups and Penalties**:
  - **60% chance** to drop a reward or penalty when an enemy is destroyed.  
  - A special power-up grants **enhanced firing mode** (triple-direction shooting) for 5 seconds.  
- **Game States**:
  - Title Screen with **start and exit** instructions.  
  - **Real-time score tracking** during gameplay.  
  - **Game Over** screen with **final score and restart options**.  

---

## 🎮 Controls  

- **LEFT ARROW** → Move left  
- **RIGHT ARROW** → Move right  
- **SPACEBAR** → Shoot  
- **P** → Pause/Resume  
- **R** → Restart after Game Over  
- **ESCAPE** → Return to Title Screen or Exit  

---

## ⚙️ Installation and Execution  

### 🔹 Specify the source directory:  
```
src/
```

### 🔹 Compile the source code:  
```sh
javac SpaceInvader.java GameManager.java
```

### 🔹 Run the game:  
```sh
java SpaceInvader main.png player.png enemy.png reward.png punishment.png
```

### 🔹 Specify the assets folder location:  
```
src/
```

---


