files = dir('*.txt') ;   % you are in the folder of files 
N = length(files) ;
% ==========read each file by loop==============
BigTable=[];
for i = 1:N
    thisfile = files(i).name ;
    % do what you want 
    % get median
    time = readtable(thisfile,'ReadVariableNames', false);
    time = median(time.Variables);
    % get data info
    metaData = split(thisfile,"_");
    metaData = [extractBefore(metaData(1),2),extractBefore(metaData(2),"c"),metaData(3),metaData(4)];
%     x = cellfun(@str2num,x);%conver cell array to number arry
    thisRow=cat(2,string(metaData),time);
    BigTable=cat(1,BigTable,thisRow); %row = observation
end

% ==========show results=============
%????????????
%???????????
col1=2;
col2=3;
filter1="0";
filter2="500000";
%?????col
col_x=1; 

%???????+??BB?BNL
row_idx = (BigTable(:, col1) == filter1 & BigTable(:, col2) == filter2);
figureData = BigTable(row_idx, :);
row_idx=(figureData(:, 4) == "BB");
figureBB = figureData(row_idx, :);
row_idx=(figureData(:, 4) == "BNL");
figureBNL = figureData(row_idx, :);

%????? + transform?double + sort???
figureBB = figureBB(:, [col_x 5]);
figureBNL = figureBNL(:, [col_x 5]);
figureBB = double(figureBB);
figureBNL = double(figureBNL);
[~,idx] = sort(figureBNL(:,1)); % sort ??? column
figureBNL = figureBNL(idx,:);   % sort the whole matrix using the sort indices
[~,idx] = sort(figureBB(:,1)); 
figureBB = figureBB(idx,:);   

%??x,y1(BB),y2(BNL)
x=figureBB(:,1);
yBB=figureBB(:,2);
yBNL=figureBNL(:,2);
%??figure
hold on
plot(x,yBB,'r',x,yBNL,'b')
legend("BB","BNL")
xlabel("data dimension") %*
ylabel("time used(nano seconds)")
xlim([min(x) max(x)])
% ylim([0 max(yBNL)/10])
title("ranked skyline of 500000 data points with 0 correlation") %*
hold off





