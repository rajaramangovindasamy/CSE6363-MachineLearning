%Rajaraman Govindasamy - K_MEANS_CLUSTER%
function k_means_cluster(datafile, k, iterations)
    inputData = dlmread(datafile);
    [x, y] = size(inputData);
    inputData = inputData(:,1:(end-1));
    for i =1: k
       cluster(1,:,i) = zeros(1,y-1,1);
       iter(i)=0;
    end
    for i = 1: x
        j = randperm(k,1);
        iter(j) = iter(j)+1;
        cluster(iter(j),:,j) = inputData(i,:);
    end
    for i = 1:k
            c_array = cluster(:,:,i);
            c_array = c_array( any(c_array,2),:);
            C(i) = length(c_array);
            c_mean(i,:) = mean(c_array);
            c_array = zeros(x,y-1);
    end
    total_error = CalcError(C,cluster, c_mean, k);
    fprintf('After initialization: error = %.4f\n', total_error);
    for z = 1 : iterations
    iter = zeros(k,1);
        for i=1: x
            Eu_dist = zeros(k,1);
           for j=1: k
              Eu_dist(j) = norm(inputData(i,:)-c_mean(j,:)); 
           end
           [value, index] = min(Eu_dist); 
           iter(index,1) = iter(index,1) + 1;
           c_new(iter(index,1),:,index) = inputData(i,:) ; 
        end   
        for j=1:k
            c_array = c_new(:,:,j);
            c_array = c_array( any(c_array,2),:);
            C(j) = length(c_array);
            c_mean(j,:) = mean(c_array);
            c_array = zeros(x,y-1);
        end
        total_error = CalcError(C,c_new, c_mean, k);
        fprintf('After iteration %d: error = %.4f\n', z, total_error);
                for j=1:k
            [cx,cy] =   size(c_new(:,:,j));
            c_new(:,:,j) = zeros(cx,cy,1);
        end  
    end
end
function [total] = CalcError(C, clust_array, clust_mean,k)
    total = 0;
    for m=1:k
       for n=1:C(m)
          total = total + norm(clust_array(n,:,m) - clust_mean(m,:));
       end
    end
end