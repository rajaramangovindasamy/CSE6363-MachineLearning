%RAJARAMAN GOVINDASAMY%
function svd_power(datafile,M,iterations) 
inputdata = importdata(datafile);
matU=[,]; u_vec = []; S = [];
matU = inputdata * transpose(inputdata);
matV = transpose(inputdata) * inputdata;
[p,q] = size(matU);
[x,y] = size(matV);
lambda = eig(matU);
[lx,ly] = size(lambda);
sort_lambda = reshape(sort(lambda(:),'descend'),[ly,lx])';
for k = 1:M
    u_vec = ones(q,1);
    u1_vec = ones(y,1);
    
    for i = 1:iterations
        B_vec = matU*u_vec;
        u_vec = B_vec/norm(B_vec);
        B1_vec = matV*u1_vec;
        u1_vec = B1_vec/norm(B1_vec);
    end
    
    matU = matU - (u_vec * (matU * u_vec)')';
    matV = matV - (u1_vec * (matV * u1_vec)')';
    u_vec(:,k)= u_vec;
    u1_vec(:,k) = u1_vec;
    
    for i = 1:q
        mvec(k,i) = u_vec(i);
        if(i==k && k<=M)
            S_S(i,k) = sqrt(sort_lambda(i,1));
        end
    end
    for d = 1:y
        m1vec(k,d) = u1_vec(d);
    end
end
U = transpose(mvec);
V = transpose(m1vec);
fprintf('\nMatrix U: \n');
for s = 1:q
    fprintf('Row %3d: ',s);
    for l = 1:M
        fprintf('%8.4f',U(s,l));
    end
    fprintf('\n');
end
fprintf('\nMatrix S: \n');
for h = 1:M
    fprintf('Row %3d: ',h);
    for k = 1:M
        fprintf('%8.4f',S_S(h,k));
    end
    fprintf('\n');
end
fprintf('\nMatrix V: \n');
for o = 1:y
    fprintf('Row %3d: ',o);
    for j = 1:M
        fprintf('%8.4f',V(o,j));
    end
    fprintf('\n');
end
RC= U*S_S*transpose(V);
[rx,ry]=size(RC);
fprintf('\nReconstruction (U*S*V''):\n');
for rc = 1:rx
    fprintf('Row %3d: ',rc);
    for  rd = 1:ry
        fprintf('%8.4f',RC(rc,rd));
    end
    fprintf('\n');
end
