%RAJARAMAN GOVINDASAMY%
function pca_power(training_file,test_file,M,iteration)
training_data = importdata(training_file);
test_data = importdata(test_file);
training_work=[,]; u = []; S = [];
training_work = training_data(:,1:end-1);
test_work = test_data(:,1:end-1);
[testrows,testcols] = size(test_work);
[trainrowm,traincols] = size(training_work);
for k = 1:M
    fprintf('Eigenvector %d\n',k);
    S = cov(training_work);
    u = ones(traincols,1);
    
    for i = 1:iteration
        B = S*u;
        u = B/norm(B);
    end
    
    training_work = training_work - (u * (training_work * u)')';
    u(:,k)= u;
    
    for i = 1:traincols
        fprintf('%3d : %.4f\n',i,u(i));
    end
end

for i = 1:testrows
    fprintf('Test object %d\n',i-1);
    for t = 1:k
        p = u(:,t)' * test_work(i,:)';
        fprintf(' %d: %.4f\n',t,p);
    end
end