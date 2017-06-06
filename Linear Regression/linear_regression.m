%RAJARAMAN GOVINDASAMY%
function linear_regression(training_file,degree,lambda) %Assignment 4, UTA ID: 1001165700%
data = load(training_file);
X = data(:, 1);
y = data(:, 2);
m=length(X);
N = degree +1;
phi = zeros(m,N);
for i = 1:N
    for j = 1:m
        phi(j,i) = X(j,1).^(i-1); %Basis function to compute phi matrix - Slide 11%
    end
end
I = eye(N); %the MxM identity matrix%
if(lambda ==0)
    weights = pinv(phi)*(y);
else
weights = inv(lambda*I+ phi'*phi)*(phi'*(y)); %equation to calculate weights- slide 57%
end
w_ele = numel(weights);
for k = 1:N
    fprintf('w%i=%.4f\n',k-1,weights(k));
end
if(degree==1) %handles W2 to zero when degree is 1%
    fprintf('w%i=%.4f\n',w_ele,0);
end
end