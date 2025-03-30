#include <stdio.h>
#include <stdlib.h>


// ��ȡ�����е����ֵ���ҳ���������λ�� 
int getMax(int a[], int n) {
	
    int max = a[0];
    
    for (int i = 1; i < n; i++) {
        if (a[i] > max) {
            max = a[i];
        }
    }
    
    return max;
}

// ��ָ��λ�����м�������
void countSort(int a[], int len, int exp) {
	
    const int BASE = 10; // ����Ϊ10��СͰ 
   
    int count[BASE] = {0}; // ��ʼ������Ͱ

    // ͳ�Ƶ�ǰλ�ĳ��ִ����������������� 
    for (int i = 0; i < len; i++) {
    	
        int digit = (a[i] / exp) % BASE;
        
        count[digit]++;
        
    }

     // ���ÿ��Ԫ���������������ұ߽�λ��
    for (int i = 1; i < BASE; i++) {
        count[i] += count[i - 1];
    }
    
    
    
    // ������ʱ������������
    int *result = (int *)malloc(len * sizeof(int));

    // ������䵽�������
    for (int i = len - 1; i >= 0; i--) {
        int digit = (a[i] / exp) % BASE;
        result[count[digit] - 1] = a[i]; 
        count[digit]--;
    }

    // �����������ƻ�ԭ����
    for (int i = 0; i < len; i++) {
        a[i] = result[i];
    }

    free(result);
}




void RadixCountSort(int a[], int len) {
	
    if (len <= 0) return;

    // ��ȡ���ֵ��ȷ��λ��
    int max = getMax(a, len);

    // �Ӹ�λ�����λ��������
    for (int i = 1; max / i > 0; i *= 10) {
        countSort(a, len, i);
    }
    
}




main() {
    
    int a[] = {236, 415, 328, 180, 324, 454, 895, 856, 637, 694};
    int a_len = sizeof(a) / sizeof(a[0]);
    

    RadixCountSort(a, a_len);
    
	 for (int i = 0; i < a_len; i++) {
        printf("%d ", a[i]);
    }

}







