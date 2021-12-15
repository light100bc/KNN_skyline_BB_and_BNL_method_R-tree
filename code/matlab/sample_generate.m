%generate and save data
%marginal dist are uniform,rho control correlation,range[0,1000000]
rng default  % For reproducibility
% test samples
dim=[2,3,4];
numPoint=[1,2,3,4,5,6,7,8,9,10];
numPoint=numPoint*100000;
cov=[   -0.8,-0.5,0,0.5,0.8
        -0.5,-0.3,0,0.5,0.8
        -0.3,-0.15,0,0.5,0.8];
% visual samples
% dim=[2,3];
% numPoint=[3000];
% cov=[-0.8,0,0.8;
%      -0.4,0,0.8];

figureCount=1;
for i = int64(dim)
    for j =cov(i-1,:)
        cov_mat=zeros(i)+j-eye(i)*j+eye(i);                
        for z =numPoint
            u = copularnd('Gaussian',cov_mat,z); %sample from [0,1]
            u=round(u*1000000);%rescale
            save(string(i)+'d_'+string(j)+'cor_'+string(z)+'.txt','u','-ascii','-tabs');
            if i==2
                figure(figureCount)
                plot(u(:,1),u(:,2),'.')
                grid on
                figureCount=figureCount+1;
            elseif i==3
                figure(figureCount)
                plot3(u(:,1),u(:,2),u(:,3),'.')
                grid on
                figureCount=figureCount+1;
            end
        end
    end
end

% only generate one sample
% numPoint=1000000;
% cov=-0.3;
% cov_mat = [1,cov,cov;cov,1,cov;cov,cov,1]; %correlation control!!????-0.5
% % cov_mat = [1,n;n,1];
% u = copularnd('Gaussian',cov_mat,numPoint); %sample from [0,1]
% u=round(u*1000000);%rescale
% save('3d_'+string(cov)+'cor_'+string(numPoint)+'.txt','u','-ascii','-tabs');
% %plot data
% % scatterhist(u(:,1),u(:,2))
% plot3(u(:,1),u(:,2),u(:,3),'.')
% grid on
