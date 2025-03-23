#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#define SNLEN sizeof(struct StackNode)


// 联合类型定义
typedef union {
    double num;  // 存储数值
    char op;     // 存储运算符
} ElemType;




// 链栈节点
typedef struct StackNode {
    ElemType data;
    struct StackNode *next;
} StackNode, *LinkStackPtr;




// 链栈结构
typedef struct LinkStack {
    LinkStackPtr top;    // 栈顶指针
    int count;           // 元素计数
} LinkStack;




// 初始化栈
void initStack(LinkStack *s) {
    s -> top = NULL;
    s -> count = 0;
}




// 获取栈顶运算符
char topOp(const LinkStack *s) {
	
    if (s -> count == 0 || s -> top == NULL) {
    	
        printf("栈为空！\n");
        exit(EXIT_FAILURE);
        
    }
    
    return s -> top -> data.op;
}




// 压入数值
void pushNum(LinkStack *s, double num) {
	
    StackNode *newNode = (StackNode*)malloc(sizeof(StackNode));

    
    // 把数据压入栈 
    newNode -> data.num = num;
    
    // 新节点的 next 指针指向栈顶 
    newNode -> next = s -> top;
    
    // 把新节点设置为栈顶
    s -> top = newNode;
    
    // 栈内元素个数+1 
    s -> count++;
    
}




// 压入运算符
void pushOp(LinkStack *s, char op) {
	
    StackNode *newNode = (StackNode*)malloc(SNLEN);
  
    newNode->data.op = op;
    
    // 新节点的 next 指针指向栈顶 
    newNode -> next = s -> top;
    
    // 把新节点设置为栈顶指针 
    s -> top = newNode;
    
    // 栈内元素个数+1    
    s -> count++;
    
    
}




// 弹出运算符
char popOp(LinkStack *s) {
	
    if (s-> count == 0 || s -> top == NULL) {
    	
        printf("栈为空！\n");
        exit(EXIT_FAILURE);
        
    }
    
    StackNode *temp = s -> top;
    char op = temp -> data.op;
    
    // 释放节点 
    s -> top = temp -> next;
    free(temp);
    
    s ->count--;
    
    // 返回头顶被弹出的符号 
    return op;
    
}



// 弹出数值
double popNum(LinkStack *s) {
	
    if (s -> count == 0 || s -> top == NULL) {
    	
        printf("栈为空！\n");
        exit(EXIT_FAILURE);
        
    }
     
    StackNode *temp = s -> top;
    double num = temp -> data.num;
    
    // 释放节点 
    s -> top = temp -> next;
    
    free(temp);
    
    s -> count--;
    return num;
}




// 规定运算符优先级
int precedence(char op) {
	
    switch(op) {
    	
        case '+': 
		case '-': return 1;
		
        case '*':
		case '/': return 2;
		
        default: return 0;
        
    }
    
}




// 执行运算
double applyOp(double a, double b, char op) {
	
    switch(op) {
        case '+': return a + b;
        case '-': return a - b;
        case '*': return a * b;
        case '/':
            if (b == 0) {
                printf("除数不能为0！\n");
                exit(EXIT_FAILURE);
            }
            return a / b;
        default:
            printf("你看到这句话说明出bug了，正常情况执行不到这里\n");
    }
    
}




// 计算答案
double evaluate(char *expr) {
	
	// 定义数值栈、符号栈 
    LinkStack values, ops;
    
    // 初始化他们 
    initStack(&values);
    initStack(&ops);

    for (int i = 0; expr[i]; i++) {
      

        // 处理数字
        if (isdigit(expr[i]) || expr[i] == '.') {
        	
        	
            double num = 0;
            
            // 用于判断这个数是不是小数，是1，否0，默认不是 
            int decimal = 0;
            
            // 用于拼接小数 
            double ten = 1.0;
            
            // 如果它不是数字，并且不是小数点 
            while (isdigit(expr[i]) || expr[i] == '.') {
            	
            	// 如果小数点首次出现，记录 
                if (expr[i] == '.' && decimal ==0) {
                	
                    decimal = 1;
                    
                } else {
                	
                    if (decimal == 0) {
                    	
                    	// 拼接整数部分 
                        num = num * 10 + (expr[i] - '0');
                        
                    } else {
                    	
                    	// 拼接小数部分 
                        ten /= 0.1;
                        num += (expr[i] - '0') * ten;
                        
                    }
                }
                
                i++;
            }
            
            // 回退一个字符，因为上面的循环结束时又移动了一次
            i--;
            
            // 将得到的数字压入栈
            pushNum(&values, num);
            
        } else if (expr[i] == '(') {
        	
        	// 左括号直接入栈 
            pushOp(&ops, '(');
            
        } else if (expr[i] == ')') {
        	
        	// 处理右括号
            while (topOp(&ops) != '(') {
            	
            	//先把这个算式看作(1) + ( 3 * 2  ) / (5) -...
            	//然后把每个左右括号单独分割出来
				//把里边的纯加减乘除式子的值计算出来，变成数字，替换原来的括号部分 
            	
            	// 获得符号栈顶的符号 
                char op = popOp(&ops);
                
                // 获得数值栈顶的数字b 
                double b = popNum(&values);
                
                // 获得数字栈顶的数字a 
                double a = popNum(&values);
                
                // 将计算后的a、b重新压入栈 （因为是倒过来的，所以a应该在前，b在后） 
                pushNum(&values, applyOp(a, b, op));
                
            }
            
            // 弹出左括号
            popOp(&ops);
            
        } else if (expr[i] == '+' || expr[i] == '-' || expr[i] == '*' || expr[i] == '/') {
        	
        	// 当符号栈中还有符号， 
            while (ops.count > 0 && precedence(topOp(&ops)) >= precedence(expr[i])) {
            	
            	// 获得符号栈顶的符号 
                char op = popOp(&ops);
                
                // 获得数值栈顶的数字b 
                double b = popNum(&values);
                
                // 获得数字栈顶的数字a 
                double a = popNum(&values);
                
                // 将计算后的a、b重新压入栈 （因为是倒过来的，所以a应该在前，b在后） 
                pushNum(&values, applyOp(a, b, op));
            }
            
            pushOp(&ops, expr[i]);
        }
    }

    // 处理剩余运算符
    while (ops.count > 0) {
    	
        // 获得符号栈顶的符号 
        char op = popOp(&ops);
                
        // 获得数值栈顶的数字b 
        double b = popNum(&values);
                
        // 获得数字栈顶的数字a 
        double a = popNum(&values);
                
        // 将计算后的a、b重新压入栈 （因为是倒过来的，所以a应该在前，b在后） 
        pushNum(&values, applyOp(a, b, op));
        
    }

	// 最后数值栈中就剩一个结果了，把它弹出返回 
    return popNum(&values);
}



// 检查式子里是否输入了奇怪的东西 
void getExpr(char expr[]) {
	
	char check[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '.', '(', ')'};
	
	printf("请输入算式(请不要输入空格，不然无法得到正确结果)：\n>");
    
   	scanf("%s",expr);
   	
   	for (int i = 0; expr[i] != '\0'; i++) {
   		
   		int judge = 0;
   		
   		for (int j = 0; check[j] != '\0'; j++) {
   			
   			if (expr[i] == check[j]) {
   				judge = 1;
   				break;
			}	
		
		}
		
		if (judge == 0) {
			printf("式子中混入了奇怪的东西\n");
			exit(EXIT_FAILURE);
		}
   		
	}
	
}




main() {
	
    char expr[100];
    
    getExpr(expr);
    
    double result = evaluate(expr);
    
    printf("计算结果: %.6f\n", result);

    return 0;
}
















