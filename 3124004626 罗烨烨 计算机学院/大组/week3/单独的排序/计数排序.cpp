#include <stdio.h>
#include <stdlib.h>

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




 main() {
	
    int a[] = {4, 2, 2, 8, 3, 3, 1, 5};
    int n = sizeof(a) / sizeof(a[0]);


    CountSort(a, n);

    for (int i = 0; i < n; i++) {
        printf("%d ", a[i]);
    }


}





