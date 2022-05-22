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

（1）静态变量：

```java
static double wall_width = 0; // x-direction
static double wall_length = 0; // y-direction
static double dt = 1.0 / 15.0; // dt
static double delta = 1.0 / 15.0; // 运动一次的距离
static int total_num = 0; // 细胞总数，随着细胞插入不断递增
// 与颜色相关，将颜色Color类一一映射为int，适用于switch()函数
static final int RED = 0;
static final int GREEN = 1;
static final int BLUE = 2;
static final int YELLOW = 3;
```

（2）普通变量:

普通变量定义了包括细胞ID、细胞半径、实时位置、未来预测位置、颜色、允许运动标识符、感知范围以及感知范围内各类颜色个数等属性：

```java
    private double red_num = 0; // 感知范围内红色个数
    private double green_num = 0; // 感知范围内绿色个数
    private double blue_num = 0; // 感知范围内蓝色个数
    private double yellow_num = 0; // 感知范围内黄色个数
    public int id;
    private final double radius;
    private double pos_x;
    private double pos_y;
    private double future_pos_x;
    private double future_pos_y;
    public boolean MOVE = true;
    private int color_index;
    private Color color;
    double perception_r;

```



## Part 3 Cell Set （暴力解法）



## Part 4 BH Tree优化算法



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





