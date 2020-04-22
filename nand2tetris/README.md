# コンピュータシステムの理論と実装

## 使い方
### 公式ページ
https://www.nand2tetris.org/copy-of-nestedcall-1

### コマンド
#### ハードウェアシミュレータ起動(1~3, 5章)
./tools/HardwareSimulator.sh

#### アセンブラシミュレータ (4章)
./tools/Assembler.sh 

#### VMシミュレータ(7章)
./tools/VMEmulator.sh

#### CPUシミュレータ(7章)
./tools/CPUEmulator.sh

#### 作成したJavaアセンブラの使い方
$ pwd
/Users/ysaito/dev/app/workspace/boot-student/nand2tetris/projects/06/pong
$ java -jar ../assembler/build/libs/assembler.jar PongL.asm 

### かかった時間
    開始日: 2020/3/28
 - chapter1: 8h
 - chapter2: 3h
 - chapter3: 4.5h(4/5 18:00)
 - chapter4: 3h (4/7 21:30)  // キーボード入力の問題を端折った
 - chapter5: 8h (4/12 16:30)
 - chapter6: 6.5h (4/14 21:30)
 - chapter7: 10h (4/21 22:00) // BasicTest完成
