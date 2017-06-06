%RAJARAMAN GOVINDASAMY%
function modm=splitfiledata(data)
inputdata= fopen(data);
tline = fgets(inputdata);
matx=[];
while ischar(tline)
    if isempty(matx)
        matx=textscan(tline,'%s', 'delimiter',',');
    else
        matx=[matx;textscan(tline,'%s', 'delimiter',',')];
    end
    tline = fgets(inputdata);
end
fclose(inputdata);
dx_r=length(matx);
dy_c=length(matx{1});
modm=cell(dx_r,dy_c);
count=1;
for s=dx_r:-1:1
    for t=1:dy_c    
        modm{count,t}=matx{s}(t);        
        
    end
    count=count+1;
end
end
