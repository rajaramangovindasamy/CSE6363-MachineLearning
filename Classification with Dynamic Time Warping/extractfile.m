%RAJARAMAN GOVINDASAMY%
function temp_D= extractfile(data)
file = fopen(data);
t_line = fgets(file);
train_D=[];
curr_obj=-1;
curr_cls=-1;
temp_D=struct();
n=1;
while ischar(t_line)   
    t_line = fgets(file);
    if strfind(t_line,'-----') ~=0        
        temp_D(n).classLabel=curr_cls;
        temp_D(n).objectID=curr_obj;
        temp_D(n).data=train_D;
        train_D=[];
        n=n+1;
        
    elseif strfind(t_line,'class label') ==1
        temp=textscan(t_line,'%s %d', 'delimiter',':');
        curr_cls=temp(2);
        
    elseif strfind(t_line,'object ID') ==1
        temp=textscan(t_line,'%s %d', 'delimiter',':');
        curr_obj=temp(2);        
        
    elseif strfind(t_line,'sign meaning') ==1
        continue;        
        
    elseif isspace(t_line)
    elseif strfind(t_line,'dominant hand trajectory') ==1
    elseif t_line==-1
    else
        temp=textscan(t_line,'%.8f64 %.8f64');
        if isempty(train_D)
            train_D=temp;
        else
            train_D=[train_D;temp];
        end
               
    end
          
end
temp_D(n).classLabel=curr_cls;
temp_D(n).objectID=curr_obj;
temp_D(n).data=train_D;
fclose(file);
end