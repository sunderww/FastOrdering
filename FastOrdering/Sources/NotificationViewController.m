//
//  NotificationViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "NotificationViewController.h"
#import "NSManagedObject+create.h"
#import "Notification.h"
#import "NotificationCell.h"
#import "AppDelegate.h"

@interface NotificationViewController ()

@end

@implementation NotificationViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	

	[deleteButton setTitle:NSLocalizedString([deleteButton titleForState:UIControlStateNormal], @"").capitalizedString forState:UIControlStateNormal];
	noNotificationLabel.text = NSLocalizedString(noNotificationLabel.text, @"");
	[self reloadData];
}

- (void)reloadData {
	selectCount = 0;
	deleteButton.hidden = YES;

	data = [Notification allObjectsSortedWithDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"date" ascending:NO]]];
	[tableView reloadData];
	tableView.hidden = !data.count;
	noNotificationLabel.hidden = data.count;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)goBack {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return data.count;
}

- (UITableViewCell *)tableView:(UITableView *)_tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"NotificationCell";
    NotificationCell * cell = [_tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
    }
	
	cell.notification = data[indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath {
	selectCount--;
	
	if (!selectCount)
		deleteButton.hidden = YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	selectCount++;
	deleteButton.hidden = NO;
}

#pragma mark - IBAction methods

- (IBAction)deleteSelectedNotifications {
	AppDelegate * appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
	NSManagedObjectContext * context = appDelegate.managedObjectContext;

	for (NSIndexPath * indexPath in [tableView indexPathsForSelectedRows]) {
		Notification * notif = data[indexPath.row];
		[context deleteObject:notif];
	}
	
	[self reloadData];
	[appDelegate saveContext];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
