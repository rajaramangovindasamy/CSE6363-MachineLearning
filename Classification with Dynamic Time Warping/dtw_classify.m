%RAJARAMAN GOVINDASAMY%
function dtw_classify (training_file, test_file)
trainData=extractfile(training_file);
testData=extractfile(test_file);
count = length(testData);
minC=zeros(length(testData),length(trainData));
for i=1:length(testData)
   parfor j=1:length(trainData)
      minC(i,j)=costcalcuation(cell2mat(testData(i).data),cell2mat(trainData(j).data));
   end
end
total=0;
for i=1:length(testData)
    [min_dist,z]=min(minC(i,:));
    predicted_class=cell2mat(trainData(z).classLabel);
    true_class=cell2mat(testData(i).classLabel);
    if(predicted_class==true_class)
        accuracy=1;    
    else
        accuracy=0;
    end
    total=total+accuracy;
    fprintf('ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f, distance = %.2f\n', i, predicted_class,true_class, accuracy, min_dist);
end
fprintf('classification accuracy=%6.4f\n', total/count);
end

