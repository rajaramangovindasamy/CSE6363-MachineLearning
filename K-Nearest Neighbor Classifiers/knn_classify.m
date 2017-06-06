%RAJARAMAN GOVINDASAMY%
function knn_classify(training_file,test_file,K)
traindata = load(training_file);
testdata = load(test_file);
x_work = traindata(:,1:end-1);
X2 = traindata(:,end);
y_work = testdata(:,1:end-1);
Y2 = testdata(:,end);
[xx,xy] = size(x_work);
[yx,yy] = size(y_work);
x_mean = mean(x_work);
x_std = std(x_work);
for i = 1:xy
    for j = 1:xx
        t1(j,i) = (x_work(j,i) - x_mean(i))/x_std(i);
    end
end
t1(1:10,:);
for i = 1:yy
    for j = 1:yx
        t2(j,i) = (y_work(j,i) - x_mean(i))/x_std(i);
    end
end
t2(1:10,:);
total=0;
Dim = zeros(xx,2);
Res = zeros(yx,1);
for i = 1:yx
    accuracy=0;
    for j = 1:xx
        for k = 1:yy
            Dim(j,1) = Dim(j,1) + (y_work(i,k) - x_work(j,k))^2;
        end
        Dim(j,1) = sqrt(Dim(j,1));
        Dim(j,2) = X2(j);
    end
    Dim = sortrows(Dim);
    Q = Dim(1:K,2);
    Res(i) = mode(Q);
    if Res(i)==Y2(i)
        total = total+1;
        accuracy=1;
    end
    fprintf('ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f\n', i-1, Res(i), Y2(i), accuracy);
    Dim = zeros(xx,2);
end
classification_accuracy=total/yx;
fprintf('classification accuracy = %6.4f\n',classification_accuracy);
end