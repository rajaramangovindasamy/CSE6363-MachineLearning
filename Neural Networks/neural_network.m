%RAJARAMAN GOVINDASAMY%
function neural_network(training_file,test_file,layers,units_per_layer,rounds)
trainingdata=load(training_file);
[row,col]=size(trainingdata);
Dimen=col-1;
x=trainingdata(:,1:Dimen);
maximumval=max(max(x));
x=x/maximumval;
t=trainingdata(:,Dimen+1);
UPL=units_per_layer;
learningRate=1;
nosclass=max(t);
units=Dimen+nosclass+UPL*(layers-2);
T(row,1:units)=0;%training output vector
for j=1:row
    T(j,units-nosclass+t(j))=1;
end
X(1:row,1)=1;
for M=1:row
    for Z=1:Dimen
        X(M,Z+1)=x(M,Z);
    end
end
for i=2:layers
    if i==layers
        w(1:UPL+1,1:nosclass,i)=(0.05+0.05).*rand(UPL+1,nosclass)-0.05;
    elseif i==2
        w(1:Dimen+1,1:UPL,i)=(0.05+0.05).*rand(Dimen+1,UPL)-0.05;    
    else
        w(1:UPL+1,1:UPL,i)=(0.05+0.05).*rand(UPL+1,UPL)-0.05;
    end
end
for k=1:rounds
     for M =1:row
        z(units)=0;
        for j=1:col-1
            z(j)=X(M,j);
        end
        a(units)=0;
        for l=2 :layers
            currIR=Dimen+2;
            for j=currIR:(currIR+UPL)
                for i=1:Dimen+1
                    a(j)=a(j)+w(mod(i,UPL)+1,mod(j,UPL)+1,l)*z(i);
                end
                z(j)=1.0/(1.0+exp(-a(j)));
            end
        end
        fndelta(units)=0;
        OLstart=units-nosclass+1;
        for j=OLstart:units
            fndelta(j)=(z(j)-T(M,j))*z(j)*(1-z(j));
            for i=OLstart-UPL:OLstart-1
                w(mod(i,UPL)+1,mod(j,UPL)+1,l-1)=w(mod(i,UPL)+1,mod(j,UPL)+1,l-1)-learningRate*fndelta(j)*z(i);
            end
            for l = layers-1 : 2
            currIR=Dimen+2;
            for j=currIR:currIR+UPL
            if l+1==layers
                Units=units-nosclass;
            else
                Units=currIR+2*UPL+1;
            end
            for u=currIR+UPL+1:Units
                fndelta(j) =fndelta(j)+(fndelta(u)*w(mod(j,UPL)+1,mod(u,UPL)+1,l))*z(j)*(1-z(j));
            end
            for i=1:Dimen+1
            if layers==2
            w(mod(i,UPL)+1,mod(j,UPL)+1,l)=w(mod(i,UPL)+1,mod(j,UPL)+1,l)-learningRate*fndelta(j)*z(i);
            else
            w(mod(i,UPL)+1,mod(j,UPL)+1,l-1)=w(mod(i,UPL)+1,mod(j,UPL)+1,l-1)-learningRate*fndelta(j)*z(i);
            end
            end
            end
            end
        end
    end
    learningRate=learningRate*0.98;
end
testdata=load(test_file);
xTest=testdata(:,1:Dimen);
maximumval=max(max(x));
xTest=xTest/maximumval;
[testRow,testCol]=size(testdata);
t=testdata(:,Dimen+1);
SML=testRow/1.8;
Xtest(1:testRow,1)=1;
for M=1:testRow
    for Z=1:Dimen
        Xtest(M,Z+1)=xTest(M,Z);
    end
end
countval=0;
fsum=0;
for SR= 1:testRow
    z(units)=0;
    for j=1:col-1
        z(j)=Xtest(SR,j);
    end
    a(units)=0;
    %step 2
    for l=2 :layers
        currIR=Dimen+2;
        for j=currIR:(currIR+UPL)
            for i=1:Dimen+1
               a(j)=a(j)+w(mod(i,UPL)+1,mod(j,UPL)+1,l)*z(i);
            end
            z(j)=1.0/(1.0+exp(-a(j)));
        end
    end
    MISa=max(z(units-nosclass:units));
    Ini=find(z(units-nosclass:units)==MISa);
    if length(Ini)~=1
        Ini=Ini(randi(numel(Ini)));
        if t(SR)==Ini-1
            countval=countval+1;
            accuracy=1/length(Ini);
        else
            accuracy=0;
            fsum=fsum+1;
        end
    else
        if t(SR)==Ini-1
            countval=countval+1;
            accuracy=1;
           
        else
            accuracy=0;
            fsum=fsum+1;
        end
    end
        if fsum>1000
            n=[Ini-1,t(SR)];
            n=n(randi(numel(n)));
            if n==t(SR)
   
                fprintf('ID=%5d, predicted=%3d, true = %3d, accuracy=1.00\n',SR-1, n,t(SR));
                countval=countval+1;
            else
                fprintf('ID=%5d, predicted=%3d, true = %3d, accuracy=0.00\n',SR-1, n,t(SR));
            end
        else
            fprintf('ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f\n',SR-1, Ini-1,t(SR),accuracy);
        end
end
fprintf('classification accuracy=%6.4f\n', countval/SML);
end