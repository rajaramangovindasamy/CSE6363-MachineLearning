%RAJARAMAN GOVINDASAMY%
function minC=costcalcuation(x,y)
[xm,~]=size(x);
[ym,~]=size(y);
cost=zeros(xm,ym);
cost(1,1)=norm(x(1,:)-y(1,:));
for i=2:xm
    cost(i,1)=cost(i-1,1)+norm(x(i,:)-y(1,:));
end
for j=2:ym
cost(1,j)=cost(1,j-1)+norm(x(1,:)-y(j,:));
end
for i=2:xm
    for j=2:ym
        minimum_C=min([cost(i-1,j),cost(i,j-1),cost(i-1,j-1)]);
       cost(i,j)=minimum_C+norm(x(i,:)-y(j,:));
    end
end
minC=cost(xm,ym);
end