matID = fopen('matrices.dat', 'w');
resID = fopen('matrices.solution.dat','w');

k = 16;
x = round(2*rand(3,3,k));

res = eye(3);
for i = 1:k
    res = res * x(:,:,i);
end

for i = 1:k
    for j = 1:3
        fprintf(matID,'%d,%d,%d;',x(j,:,i));
    end
    fprintf(matID,'\n');
end

for j = 1:3
    fprintf(resID,'%d,%d,%d;',res(:,j));
end


res
