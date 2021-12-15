filename="2d_0.8cor_3000";
res=load(filename+"_BB.txt");
org=load(filename+".txt");


plot(org(:,1),org(:,2),'bo',res(:,2),res(:,3),'r*') % ????distance??
% plot3(org(:,1),org(:,2),org(:,3),'bo',res(:,2),res(:,3),res(:,4),'r*')% ????distance??
legend("original","skyline")
title(" 2 dimension, 3000 points with 0.8 correlation")
grid on
% saveas(gcf,filename+'.png')