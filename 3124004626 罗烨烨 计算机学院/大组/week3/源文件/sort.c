#include <stdio.h>
#include <stdlib.h>


#define BASE 10


// �������������� 
void InsertSort(int arr[], int n) {
	
    int i, j, key;
    
    for (i = 1; i < n; i++) {
    	
    	// ȡ����ǰ������Ԫ��
        key = arr[i];
        j = i - 1;

        // ������key��Ԫ�غ��� 
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        
        // ���뵽��ȷλ��
        arr[j + 1] = key;
    }
}



// �鲢���������������ϲ���������������
void merge(int a[], int left, int mid, int right) {
	
    int i, j, k;
    int n1 = mid - left + 1;
    int n2 = right - mid;

    // ������ʱ����
    int *L = (int *)malloc(n1 * sizeof(int));
    int *R = (int *)malloc(n2 * sizeof(int));

    // �������ݵ���ʱ����
    for (i = 0; i < n1; i++)
        L[i] = a[left + i];
        
    for (j = 0; j < n2; j++)
        R[j] = a[mid + 1 + j];

    
    i = 0;    
    j = 0;    
    k = left; 
    
    // �ϲ���ʱ�����ԭ����
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            a[k] = L[i];
            i++;
        } else {
            a[k] = R[j];
            j++;
        }
        k++;
    }

    // ����ʣ��Ԫ��
    while (i < n1) {
        a[k] = L[i];
        i++;
        k++;
    }
    while (j < n2) {
        a[k] = R[j];
        j++;
        k++;
    }

    free(L);
    free(R);
}



// �鲢����������
void MergeSort(int a[], int left, int right) {
	
    if (left >= right) {
    	return;
    }
    
    int mid = left + (right - left) / 2;

    // �ݹ���� 
    MergeSort(a, left, mid);
    MergeSort(a, mid + 1, right);

    // �ϲ��������������
    merge(a, left, mid, right);
    
}



// �������������� 
void QuickSort(int a[], int left, int right) {
	
    // �����߽糬Խ�ұ߽磬��ֹ
    if (left > right) {
       return;
    }

    int start = left;
    int end = right;
    int baseNum = a[start];
    while (start != end) {

        // �����߽�С���ұ߽磬��startָ������ִ��ڻ�׼�����ƶ��ұ߽�end
        while ( (end > start) && (a[end] >= baseNum) ) {
            end--;
        }

        // �����߽�С���ұ߽磬��startָ�������С�ڻ�׼�����ƶ���߽�start
        while ( (start < end) && (a[start] <= baseNum) ) {
            start++;
        }

        // ��������������
        int temp = a[start];
        a[start] = a[end];
        a[end] = temp;

    }

    // start��end�غ�ʱ�������ǹ�ָͬ�����������߽绥��
    a[left] = a[start];
    a[start] = baseNum;

    // �ݹ����
    QuickSort(a, left, end - 1);
    QuickSort(a, start + 1, right);
        
}



// �������������� 
void CountSort(int *a, int n) {
	
    if (n <= 0) return;

    // �ҳ������е����ֵ����Сֵ
    int max = a[0];
    int min = a[0];
    for (int i = 1; i < n; i++) {
        if (a[i] > max) max = a[i];
        if (a[i] < min) min = a[i];
    }

     
    int range = max - min + 1;

    // ��������ʼ����������
    int *count = (int *)calloc(range, sizeof(int));

    // ͳ��ÿ��Ԫ�صĳ��ִ���
    for (int i = 0; i < n; i++) {
        count[a[i] - min]++;
    }

    // ���ÿ��Ԫ���������������ұ߽�λ��
    for (int i = 1; i < range; i++) {
        count[i] += count[i - 1];
    }

    // ������ʱ������������
    int *result = (int *)malloc(n * sizeof(int));


    // ��������������
    for (int i = n - 1; i >= 0; i--) {
        result[count[a[i] - min] - 1] = a[i];
        count[a[i] - min]--;
    }

    // �����������ƻ�ԭ����
    for (int i = 0; i < n; i++) {
        a[i] = result[i];
    }

   
    free(count);
    free(result);
}




// ������������������������ȡ�����е����ֵ���ҳ���������λ�� 
int getMax(int a[], int n) {
	
    int max = a[0];
    
    for (int i = 1; i < n; i++) {
        if (a[i] > max) {
            max = a[i];
        }
    }
    
    return max;
}



// ������������������������ָ��λ�����м�������
void countSort(int a[], int len, int exp) {
	  
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



// ������������������
void RadixCountSort(int a[], int len) {
	
    if (len <= 0) return;

    // ��ȡ���ֵ��ȷ��λ��
    int max = getMax(a, len);

    // �Ӹ�λ�����λ��������
    for (int i = 1; max / i > 0; i *= 10) {
        countSort(a, len, i);
    }
    
}







