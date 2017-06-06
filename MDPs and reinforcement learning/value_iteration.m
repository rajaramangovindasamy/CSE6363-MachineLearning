%RAJARAMAN GOVINDASAMY%
function value_iteration(environment_file,non_terminal_reward,gamma,K)
global actual_value;
global U;
actual_value=splitfiledata(environment_file);
[d_x,d_y]=size(actual_value);
R=zeros(d_x,d_y);
for i=1:d_x
    for j=1:d_y
        if(strcmp(actual_value{i,j},'-1.0')) 
            R(i,j)=-1;
        elseif(strcmp(actual_value{i,j},'1.0'))
            R(i,j)=1;
        elseif(strcmp(actual_value{i,j},'.'))
            R(i,j)=non_terminal_reward;
        elseif(strcmp(actual_value{i,j},'X'))   
            R(i,j)=0;
        end
    end
end
U=zeros(d_x,d_y);
Ub=zeros(d_x,d_y);
for k=1:K
    U=Ub;
    for i=1:d_x
        for j=1:d_y
            Ub(i,j)=R(i,j)+gamma*calculateProbability(i,j);        
        end
    end
end
%display output of last iteration%
for i=d_x:-1:1
    for j=1:d_y
        if(i==d_x&&j==d_y)
            fprintf('%6.3f',U(i,j));
        else
            fprintf('%6.3f',U(i,j));
        end
        if(j~=d_y)
            fprintf(',');
        end
    end
    fprintf('\n');
end
%function to calculate maximum probability%
function max_a=calculateProbability(i,j)
[d_x,d_y]=size(actual_value);
probability_a=zeros(1,4);
val=string(actual_value{i,j});
if(val~='X' && val~='1.0' && val~='-1.0')
%condition to move upwards%
if(i+1<=d_x)
    probability_a(1)=0.8*U(i+1,j);
else
    probability_a(1)=0.8*U(i,j);
end
if(j+1<=d_y)
    probability_a(1)=probability_a(1)+(0.1*U(i,j+1));
else
    probability_a(1)=probability_a(1)+(0.1*U(i,j));    
end    
if(j-1>=1)
    probability_a(1)=probability_a(1)+(0.1*U(i,j-1));
else
    probability_a(1)=probability_a(1)+(0.1*U(i,j));
end
%condition to move downwards%
if(i-1>=1)
    probability_a(2)=(0.8*U(i-1,j));
else
    probability_a(2)=(0.8*U(i,j));
end    
if(j+1<=d_y)
    probability_a(2)=probability_a(2)+(0.1*U(i,j+1));
else
    probability_a(2)=probability_a(2)+(0.1*U(i,j));
end    
if(j-1>=1)
    probability_a(2)=probability_a(2)+(0.1*U(i,j-1));
else
    probability_a(2)=probability_a(2)+(0.1*U(i,j));
end
%condition to move right%
if(j+1<=d_y)
    probability_a(3)=(0.8*U(i,j+1));
else
     probability_a(3)=(0.8*U(i,j));
end
if(i-1>0)
    probability_a(3)=probability_a(3)+(0.1*U(i-1,j));
else
    probability_a(3)=probability_a(3)+(0.1*U(i,j));
end    
if(i+1<=d_x)
    probability_a(3)=probability_a(3)+(0.1*U(i+1,j));
else
    probability_a(3)=probability_a(3)+(0.1*U(i,j));
end    
%condition to move left%
if(j-1>0)
    probability_a(4)=(0.8*U(i,j-1));
else
    probability_a(4)=(0.8*U(i,j));
end
if(i-1>0)
    probability_a(4)=probability_a(4)+(0.1*U(i-1,j));
else
    probability_a(4)=probability_a(4)+(0.1*U(i,j));
end    
if(i+1<=d_x)
    probability_a(4)=probability_a(4)+(0.1*U(i+1,j));
else
    probability_a(4)=probability_a(4)+(0.1*U(i,j));
end
end    
max_a=max(probability_a);
end
end