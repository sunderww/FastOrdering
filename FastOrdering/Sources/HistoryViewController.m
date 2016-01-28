//
//  HistoryViewController.m
//  FastOrdering
//
//  Created by Sunder on 27/04/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "HistoryViewController.h"
#import "OrderCell.h"
#import "Order+Custom.h"
#import "NSManagedObject+create.h"

#define kHistoryOrderLoadCount  20

@interface HistoryViewController ()

@end

@implementation HistoryViewController

- (void)viewDidLoad {
	[super viewDidLoad];

	noOrdersLabel.text = NSLocalizedString(noOrdersLabel.text, @"");
	[self reloadData];
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	// Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)reloadData {
	data = [NSMutableArray new];
	[self appendOrders:kHistoryOrderLoadCount];

	orders.hidden = !data.count;
	noOrdersLabel.hidden = data.count;
}

- (void)appendOrders:(NSInteger)count {
	[data addObjectsFromArray:[Order last:count skip:data.count withDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO]]]];
	[orders reloadData];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return data.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString * CellIdentifier = @"OrderCell";
	
	OrderCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		cell = [[NSBundle mainBundle] loadNibNamed:@"OrderCell" owner:self options:nil][0];
	}
	
	[cell setOrder:data[indexPath.row]];
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	Order * order = data[indexPath.row];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
	[self.mainController goToOrder:order];
}

#pragma mark - UIScrollView delegate methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
	CGFloat actualPosition = scrollView.contentOffset.y;
	CGFloat contentHeight = scrollView.contentSize.height - (scrollView.frame.size.height);
	if (actualPosition >= contentHeight) {
		[self appendOrders:kHistoryOrderLoadCount];
	}
}

@end
