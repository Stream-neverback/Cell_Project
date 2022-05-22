# CS203B Project - RGBY Cell Life

<font size=3>**Group Member: 丁辰辰 江轶豪 谭雍昊**  

**SID: **11912401 11912404 11910204

## Part 1 概述

在本次项目中，我们需要按照项目要求实现四色细胞的变色与移动功能，每种细胞的变色与移动均有不同，且规则明确。我们分别尝试暴力解法、BH树优化算法与KD树优化算法，其中我们最后采用的KD树优化算法可以达到极高的运行效率。与此同时，在正确性测试与运行效率测试中我们的程序均能完美通过。在程序运行上，我们采用脚本工具简化运行方式且方便老师和TA批改。在基础部分之外，我们还是实现了两个额外功能：随机更改细胞颜色与删除细胞功能。

我们项目的github地址为https://github.com/Stream-neverback/Cell_Project，其中还附有项目展示的视频，包括基本功能与额外功能，欢迎star！

## Part 2 Cell类

Cell类集中了对Cell属性定义和操作与动态性相关的构造和方法

```java
public class Cell implements Comparable<Cell>
```

其中Cell类里定义了包括了有

##### （1）静态变量：

```java
static double wall_width = 0; // x-direction 墙宽
static double wall_length = 0; // y-direction 墙长
static double dt = 1.0 / 15.0; // dt
static double delta = 1.0 / 15.0; // 运动一次的距离
static int total_num = 0; // 细胞总数，随着细胞插入不断递增
// 与颜色相关，将颜色Color类一一映射为int，适用于switch()函数
static final int RED = 0;
static final int GREEN = 1;
static final int BLUE = 2;
static final int YELLOW = 3;
```

##### （2）普通变量:

普通变量定义了包括细胞ID、细胞半径、实时位置、未来预测位置、颜色、允许运动标识符、感知范围以及感知范围内各类颜色个数等属性：

```java
    private double red_num = 0; // 感知范围内红色个数
    private double green_num = 0; // 感知范围内绿色个数
    private double blue_num = 0; // 感知范围内蓝色个数
    private double yellow_num = 0; // 感知范围内黄色个数
    public int id; // 细胞ID，数值上等于插入细胞时的总数，用后续区分是否为同一个细胞
    private final double radius; // 细胞半径
    private double pos_x; // 细胞x坐标
    private double pos_y; // 细胞y坐标
    private double future_pos_x; // 细胞未来预测x坐标（无遮挡物）
    private double future_pos_y; // 细胞未来预测y坐标（无遮挡物）
    public boolean MOVE = true; // 是否允许正常运动（即是否不需考虑有无物体遮挡的运动）
    private int color_index; // 颜色序号，即颜色Color类的映射
    private Color color; // 颜色
    double perception_r; // 感知范围

```

##### （3）细胞Cell类和墙的构造

```java
public Cell(double radius, double position_x, double position_y, Color color, double perception_radius) 
public Cell(Color color)
public Cell() 
```

##### （4）墙的构造

```java
public static void initWall(double wX, double wY)
```

##### （5）常规方法

###### i. 通用方法

细胞距离计算，包括未来预期距离（用于运动判断）

```java
public double distanceTo(Cell other)
public double x_distanceTo(Cell other)
public double y_distanceTo(Cell other)
public double future_distanceTo(Cell other)
public double distanceSquaredTo(Cell that)
```

细胞属性的返回和其他基本操作

```java
public void draw() // 绘制细胞
public double getX()
public double getY()
public double getRadius()
public double getPerception_r()
public Color getColor()
```

###### ii. 关于细胞运动的方法

关于细胞的正常运动（*MOVE = true*），下方代码以红色细胞为例，需要判断该红色细胞的运动标识符*MOVE = true*，并且确保下一步的运动不会撞到墙上，如果会撞到墙上，则运动到贴近墙边。

```java
 public void move(){
 switch (this.color_index) {
     case RED:
     if (this.pos_y + delta < wall_length - this.radius && this.MOVE) {
		this.pos_y = this.pos_y + delta;
	} else if (this.pos_y + delta >= wall_length - this.radius && this.MOVE) {
		this.pos_y = wall_length - this.radius;
		}
	 ...
	break;
 ...
 }
```

该方法并非实际上使得细胞发生运动，而是赋给*future_pos_x*或*future_pos_y*值，用于后续判断是否会碰撞接触

```java
public void future_move()
```

判断细胞**是否即将与其他细胞**发生接触，与*future_distanceTo(Cell other)*搭配使用：

```java
public boolean Cell_Overlap(Cell other)
```

返回细胞即将与其他一个会发生碰撞的细胞所需运动的距离，可用几何学方法求得数学解：

```java
public double unitDistanceUntilContact(Cell cell)
```

区别于正常运动*move()*，该方法会使细胞运动一个小于1/15的距离直到与其他一个细胞发生接触：

```java
public void moveUntilContact(Cell cell)
```

判断细胞是否会出界

```java
public boolean isOut()
```

###### iii. 关于细胞颜色检测的方法

对于探测范围内不同细胞的颜色，感知到之后需要进行统计（下方代码以感知到*cell*为红色为例）：

```java
public void add_num(Cell cell) {
	switch (cell.getColorIndex()) {
		case RED:
		this.red_num += 1;
		break;
		...
	}
}
```

根据项目要求，对细胞颜色进行逻辑判断，并根据规则进行颜色变化（以红色细胞判断逻辑为例）：

```java
 public void check_color() {
	double sum_num = this.red_num + this.blue_num + this.green_num + this.yellow_num;
	// check red
    if (this.color == Color.RED) {
		if (this.red_num >= 3 && this.red_num / sum_num > 0.7) {
			this.color = Color.GREEN;
            this.color_index = GREEN;
            this.MOVE = true;
		} else if (this.yellow_num >= 1 && this.yellow_num / (sum_num) < 0.1) {
			this.color = Color.YELLOW;
            this.color_index = YELLOW;
            this.MOVE = true;
            }
	}
	...
 }
```

###### iv. 与BHTree相关

判断细胞是否在BHTree的（东北、东南、西南或西北）树内：

```java
public boolean in(QuadNode q) {
	return q.contains(this.pos_x, this.pos_y);
}
```

###### v. 与KdTree相关

判断细胞是否在Kdtree划定的矩形区域内，用于颜色检测：

```java
public boolean in(QuadNode q) {
	return q.contains(this.pos_x, this.pos_y);
}
```



## Part 3 Cell Set （暴力解法）



## Part 4 BH Tree优化算法

与BHtree相关的代码文件位于bhtree文件夹内，包括BHTree.java和QuadNode.java

对于BHTree类，定义以下属性：

```java
private Cell cell;
private final QuadNode qNode;
private BHTree nwChild;
private BHTree neChild;
private BHTree swChild;
private BHTree seChild;
```

并进行BHTree的构造

```java
public BHTree(QuadNode q) {
	this.qNode = q;
    this.cell = null;
    this.nwChild = null;
    this.neChild = null;
    this.swChild = null;
    this.seChild = null;
}
```

我们可以将BHTree理解为一个四叉树，每一个节点都有一个细胞

对于每一个节点，即QuadNode类，定义有：

```java
private final double x_center;
private final double y_center;
private final double length_x;
private final double length_y;
```

从一个矩形开始，每插入一个细胞，实现将一个矩形不断地四等分的数据结构，如果没有子树，则建立子树，再进行插入，插入规则取决于细胞所处的位置和QuadNode的位置与矩形范围：

```java
public void insert(Cell cell) {
	if (this.qNode.contains(cell.getX(), cell.getY())) {
		if (this.cell == null) {
			this.cell = cell;
			return;
         }
    if (!hasNoLeaf()) { // have subtrees internal node
        insertChild(cell);
    } else { // external node
        this.createSubTrees();
        insertChild(cell);
    	}
    }
    else{
        StdOut.println("Warning: Cell is out of BHTree!");
        }
}

private void insertChild(Cell b) {
    if (b.in(nwChild.qNode))
        nwChild.insert(b);
    else if (b.in(neChild.qNode))
        neChild.insert(b);
    else if (b.in(seChild.qNode))
        seChild.insert(b);
    else if (b.in(swChild.qNode))
        swChild.insert(b);
}

public void createSubTrees() {
    this.nwChild = new BHTree(qNode.NW_Q());
    this.neChild = new BHTree(qNode.NE_Q());
    this.swChild= new BHTree(qNode.SW_Q());
    this.seChild = new BHTree(qNode.SE_Q());
}

private boolean hasNoLeaf() {
    return (nwChild == null && neChild == null && swChild == null && seChild == null);
}
```

后续关于该类数据结构在运动和颜色的判断，我们只需要根据圆或矩形筛选出在范围内的QuadNode，从而筛选出QuadNode内的细胞进行判断，这样就可以省去了遍历所消耗的复杂度O(N^2)。但由于BHTree在各种样本的测试中并**不会显著优于**暴力解法，故略去相关说明，只提供对应方法：

```java
package proj.bhtree
public void checkCollision(Cell cell)
public void checkDetection(Cell cell) 
```



## Part 5 KD Tree优化算法



## Part 6 评估

按照项目文档要求，我们将会对数据准确度进行测试，同时还会对运行效率进行测试。

#### A. 数据准确度测试

| 测试集  | 错误项数 | 测试项数 | 错误率 |
| ------- | -------- | -------- | ------ |
| Sample1 | 0        | 3        | 0.0%   |
| Sample2 | 0        | 500      | 0.0%   |
| Sample3 | 8        | 500      | 1.6%   |

在Terminal模式下使用`run.bat`运行程序，并且使用`compare.bat`脚本进行结果对比，结果如上表格所示。在`sample1`与`sample2`的测试中，输出结果与正确结果完全一致。而在`sample3`的测试中，由于数据误差带来了8个错误项数，但是从下图输出的差异来看，仅有单位时间的差异，可忽略不计。

![image-20220522173000805](C:\Users\Meteor\AppData\Roaming\Typora\typora-user-images\image-20220522173000805.png)

据此，数据准确度测试通过。

#### B. 运行效率测试

针对运行效率的测试，我们选取的指标为每秒帧数，即每秒帧数越大越好，则运行参数选择`benchmark`放开帧率限制。同时，测试环境应为细胞未稳定前的状态。测试电脑配置为英特尔8代i5笔记本芯片。

| 测试集  | 运行模式 | 平均帧率 |
| ------- | -------- | -------- |
| Sample1 | GUI      | 178.1    |
| Sample1 | Terminal | 6232.4   |
| Sample2 | GUI      | 29.6     |
| Sample2 | Terminal | 128.7    |
| Sample3 | GUI      | 27.1     |
| Sample3 | Terminal | 87.4     |

测试结果如上表格所示。在Terminal模式下，所有测试集下运行效率都远大于要求的每秒15帧，而GUI模式下，仍然能达到要求的两倍左右效率。

据此，运行效率测试完美通过。

## Part 7 额外工作

在实现基本功能正确无误的前提下，我们还发挥想象力，创造出两种特殊的模式。模式的展示也附在项目文件中的展示视频中，欢迎尝试！

#### A. 随机更改颜色模式

在此模式下，点击图中的细胞可以将其颜色随机改变为四种颜色中的另一种。

#### B. 删除细胞模式

在此模式下，点击图中的细胞会将其删除，比如`sample2`中随机删除细胞后，可以使得原本阻塞的通道打开，使细胞能够移动，十分有趣。

## Part 8 遇到的问题与解决方案





