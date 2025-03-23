#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

#define SNLEN sizeof(struct StackNode)


// �������Ͷ���
typedef union {
    double num;  // �洢��ֵ
    char op;     // �洢�����
} ElemType;




// ��ջ�ڵ�
typedef struct StackNode {
    ElemType data;
    struct StackNode *next;
} StackNode, *LinkStackPtr;




// ��ջ�ṹ
typedef struct LinkStack {
    LinkStackPtr top;    // ջ��ָ��
    int count;           // Ԫ�ؼ���
} LinkStack;




// ��ʼ��ջ
void initStack(LinkStack *s) {
    s -> top = NULL;
    s -> count = 0;
}




// ��ȡջ�������
char topOp(const LinkStack *s) {
	
    if (s -> count == 0 || s -> top == NULL) {
    	
        printf("ջΪ�գ�\n");
        exit(EXIT_FAILURE);
        
    }
    
    return s -> top -> data.op;
}




// ѹ����ֵ
void pushNum(LinkStack *s, double num) {
	
    StackNode *newNode = (StackNode*)malloc(sizeof(StackNode));

    
    // ������ѹ��ջ 
    newNode -> data.num = num;
    
    // �½ڵ�� next ָ��ָ��ջ�� 
    newNode -> next = s -> top;
    
    // ���½ڵ�����Ϊջ��
    s -> top = newNode;
    
    // ջ��Ԫ�ظ���+1 
    s -> count++;
    
}




// ѹ�������
void pushOp(LinkStack *s, char op) {
	
    StackNode *newNode = (StackNode*)malloc(SNLEN);
  
    newNode->data.op = op;
    
    // �½ڵ�� next ָ��ָ��ջ�� 
    newNode -> next = s -> top;
    
    // ���½ڵ�����Ϊջ��ָ�� 
    s -> top = newNode;
    
    // ջ��Ԫ�ظ���+1    
    s -> count++;
    
    
}




// ���������
char popOp(LinkStack *s) {
	
    if (s-> count == 0 || s -> top == NULL) {
    	
        printf("ջΪ�գ�\n");
        exit(EXIT_FAILURE);
        
    }
    
    StackNode *temp = s -> top;
    char op = temp -> data.op;
    
    // �ͷŽڵ� 
    s -> top = temp -> next;
    free(temp);
    
    s ->count--;
    
    // ����ͷ���������ķ��� 
    return op;
    
}



// ������ֵ
double popNum(LinkStack *s) {
	
    if (s -> count == 0 || s -> top == NULL) {
    	
        printf("ջΪ�գ�\n");
        exit(EXIT_FAILURE);
        
    }
     
    StackNode *temp = s -> top;
    double num = temp -> data.num;
    
    // �ͷŽڵ� 
    s -> top = temp -> next;
    
    free(temp);
    
    s -> count--;
    return num;
}




// �涨��������ȼ�
int precedence(char op) {
	
    switch(op) {
    	
        case '+': 
		case '-': return 1;
		
        case '*':
		case '/': return 2;
		
        default: return 0;
        
    }
    
}




// ִ������
double applyOp(double a, double b, char op) {
	
    switch(op) {
        case '+': return a + b;
        case '-': return a - b;
        case '*': return a * b;
        case '/':
            if (b == 0) {
                printf("��������Ϊ0��\n");
                exit(EXIT_FAILURE);
            }
            return a / b;
        default:
            printf("�㿴����仰˵����bug�ˣ��������ִ�в�������\n");
    }
    
}




// �����
double evaluate(char *expr) {
	
	// ������ֵջ������ջ 
    LinkStack values, ops;
    
    // ��ʼ������ 
    initStack(&values);
    initStack(&ops);

    for (int i = 0; expr[i]; i++) {
      

        // ��������
        if (isdigit(expr[i]) || expr[i] == '.') {
        	
        	
            double num = 0;
            
            // �����ж�������ǲ���С������1����0��Ĭ�ϲ��� 
            int decimal = 0;
            
            // ����ƴ��С�� 
            double ten = 1.0;
            
            // ������������֣����Ҳ���С���� 
            while (isdigit(expr[i]) || expr[i] == '.') {
            	
            	// ���С�����״γ��֣���¼ 
                if (expr[i] == '.' && decimal ==0) {
                	
                    decimal = 1;
                    
                } else {
                	
                    if (decimal == 0) {
                    	
                    	// ƴ���������� 
                        num = num * 10 + (expr[i] - '0');
                        
                    } else {
                    	
                    	// ƴ��С������ 
                        ten /= 0.1;
                        num += (expr[i] - '0') * ten;
                        
                    }
                }
                
                i++;
            }
            
            // ����һ���ַ�����Ϊ�����ѭ������ʱ���ƶ���һ��
            i--;
            
            // ���õ�������ѹ��ջ
            pushNum(&values, num);
            
        } else if (expr[i] == '(') {
        	
        	// ������ֱ����ջ 
            pushOp(&ops, '(');
            
        } else if (expr[i] == ')') {
        	
        	// ����������
            while (topOp(&ops) != '(') {
            	
            	//�Ȱ������ʽ����(1) + ( 3 * 2  ) / (5) -...
            	//Ȼ���ÿ���������ŵ����ָ����
				//����ߵĴ��Ӽ��˳�ʽ�ӵ�ֵ���������������֣��滻ԭ�������Ų��� 
            	
            	// ��÷���ջ���ķ��� 
                char op = popOp(&ops);
                
                // �����ֵջ��������b 
                double b = popNum(&values);
                
                // �������ջ��������a 
                double a = popNum(&values);
                
                // ��������a��b����ѹ��ջ ����Ϊ�ǵ������ģ�����aӦ����ǰ��b�ں� 
                pushNum(&values, applyOp(a, b, op));
                
            }
            
            // ����������
            popOp(&ops);
            
        } else if (expr[i] == '+' || expr[i] == '-' || expr[i] == '*' || expr[i] == '/') {
        	
        	// ������ջ�л��з��ţ� 
            while (ops.count > 0 && precedence(topOp(&ops)) >= precedence(expr[i])) {
            	
            	// ��÷���ջ���ķ��� 
                char op = popOp(&ops);
                
                // �����ֵջ��������b 
                double b = popNum(&values);
                
                // �������ջ��������a 
                double a = popNum(&values);
                
                // ��������a��b����ѹ��ջ ����Ϊ�ǵ������ģ�����aӦ����ǰ��b�ں� 
                pushNum(&values, applyOp(a, b, op));
            }
            
            pushOp(&ops, expr[i]);
        }
    }

    // ����ʣ�������
    while (ops.count > 0) {
    	
        // ��÷���ջ���ķ��� 
        char op = popOp(&ops);
                
        // �����ֵջ��������b 
        double b = popNum(&values);
                
        // �������ջ��������a 
        double a = popNum(&values);
                
        // ��������a��b����ѹ��ջ ����Ϊ�ǵ������ģ�����aӦ����ǰ��b�ں� 
        pushNum(&values, applyOp(a, b, op));
        
    }

	// �����ֵջ�о�ʣһ������ˣ������������� 
    return popNum(&values);
}



// ���ʽ�����Ƿ���������ֵĶ��� 
void getExpr(char expr[]) {
	
	char check[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '.', '(', ')'};
	
	printf("��������ʽ(�벻Ҫ����ո񣬲�Ȼ�޷��õ���ȷ���)��\n>");
    
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
			printf("ʽ���л�������ֵĶ���\n");
			exit(EXIT_FAILURE);
		}
   		
	}
	
}




main() {
	
    char expr[100];
    
    getExpr(expr);
    
    double result = evaluate(expr);
    
    printf("������: %.6f\n", result);

    return 0;
}
















