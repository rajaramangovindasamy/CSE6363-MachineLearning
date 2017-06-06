%RAJARAMAN GOVINDASAMY%
function logistic_regression(training_file,degree,test_file)
trainingdata=load(training_file);
y=trainingdata(:,end);
z=trainingdata(:,1:end-1);
[m,N]=size(z);
y2 = zeros(m,1);
n = (degree*N)+1;
for s = 1:m
    if y(s,1)~=1 
        y2(s,1)=0;
    else 
        y2(s,1)=y(s,1);
    end
end
i_vec = ones(m,1);
if(degree==1)
    temp = [i_vec z];
end
if(degree ==2)
    temp = zeros(m,n);
    for i = 1:m
        temp(i,1) = 1;
        count = 2;
        for d = 1:N
            temp(i,count) = z(i,d);
            count = count+1;
            temp(i,count) = z(i,d)*z(i,d);
            count = count+1;
        end
    end
end
w = zeros((degree*N)+1,1);
yx = zeros(m,1);
R = zeros(m,m);
weight = 1.00;
while (weight >= 0.01)
for k = 1:m
    v = temp(k,:);
    a = transpose(w)*transpose(v); 
    denmo = 1 + exp(-a);
    yx(k,1) = 1./denmo;
end
for l = 1:m
          R(l,l)=yx(l,1)*(1-yx(l,1));
end    
    H = transpose(temp)*R*temp;
    w_new = w - (inv(H)*transpose(temp)*(yx - y2));
    
    weight = 0.00;
    for t = 1:n
        weight = weight + (abs(w_new(t,1)-w(t,1)));
    end
    w = w_new;
end  
for k = 1:n
    fprintf('w%i=%.4f\n',k-1,w(k));
end
testdata=load(test_file);
[p,q] = size(testdata);
z1 = testdata(:,1:end-1);
i2vec = ones(p,1);
nos = q*6;
z2 = [i2vec z1];
    total = 0;
    predicted = 0;
    probability = 0.0;
for k = 1:p
    if(degree ==1)
    acc =transpose(w)*transpose(z2(k,:));
    end
    if(degree ==2)
        acc =(w)*(z2(k,:));
    end
    denmo = 1 + exp(acc);
    ym = 1./denmo;
    if ym > 0.5 
        predicted = 1;
        probability = ym;
    end
    if ym < 0.5
        predicted = 0;
        probability = (1-ym);
    end
    if predicted == testdata(k,q)
        accuracy = 1;
    end
    if predicted ~= testdata(k,q)
        accuracy = 0;
    end
    if ym == 0.5
        accuracy = 0.5;
    end
    total = total + accuracy;
    class = testdata(k,q);
   fprintf('ID=%5d, predicted=%3d, probability = %.4f, true=%3d, accuracy=%4.2f\n',(k-1), predicted, probability, class, accuracy);
end
total = total./nos;
fprintf('classification accuracy=%6.4f',total);
end