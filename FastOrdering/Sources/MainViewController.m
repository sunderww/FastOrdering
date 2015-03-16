//
//  MainViewController.m
//  FastOrdering
//
//  Created by Sunder on 15/10/2014.
//  Copyright (c) 2014 lucas.bergognon. All rights reserved.
//

#import "MainViewController.h"
#import "NotificationViewController.h"
#import "TablePlanViewController.h"
#import "MenuViewController.h"
#import "AboutViewController.h"
#import "AppDelegate.h"
#import "SocketHelper.h"
#import "Order.h"
#import "Notification.h"
#import "NSManagedObject+create.h"
#import "OrderCell.h"
#import "NotificationCell.h"
#import "CommandViewController.h"

#define kTableViewCellHeight    55

@interface MainViewController ()

@end

@implementation MainViewController

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

    [self syncDatabase];
    panelShown = NO;
    panelView.hidden = NO;
    overlay.alpha = 0;

    titleLabel.text = NSLocalizedString(@"Main Page", @"");
    lastNotificationsLabel.text = NSLocalizedString(@"Last Notifications", @"");
    lastOrdersLabel.text = NSLocalizedString(@"Last Orders", @"");
    noOrderLabel.text = NSLocalizedString(@"No order", @"");
    noNotificationLabel.text = NSLocalizedString(@"No notification", @"");
    for (UIButton * button in panelButtons)
        [button setTitle:NSLocalizedString(button.titleLabel.text, @"").capitalizedString forState:UIControlStateNormal];
    [orderButton setTitle:NSLocalizedString(@"order", @"").uppercaseString forState:UIControlStateNormal];
}

- (void)viewWillAppear:(BOOL)animated {
    lastOrders = [Order last:2 withDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO]]];
    lastNotifications = [Notification last:2 withDescriptors:@[[NSSortDescriptor sortDescriptorWithKey:@"updatedAt" ascending:NO]]];

    CGRect frame = lastOrdersTableView.frame;
    frame.size.height = lastOrders.count * kTableViewCellHeight;
    lastOrdersTableView.frame = frame;
    frame = lastNotificationsTableView.frame;
    frame.size.height = lastNotifications.count * kTableViewCellHeight;
    lastNotificationsTableView.frame = frame;
    
    noOrderLabel.hidden = lastOrders.count > 0;
    noNotificationLabel.hidden = lastNotifications.count > 0;

    [lastOrdersTableView reloadData];
    [lastNotificationsTableView reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)syncDatabase {
    SyncHelper * syncer = [SyncHelper new];
    
    [SocketHelper connectSocket];
    classesToSync = 0;
//
//    [[SocketHelper sharedSocket] put:@"/dish" withData:@{@"name":@"Test", @"available":@YES, @"price":@6.5} callback:^(id response) {
//        DPPLog(@"%@", [response class]);
//        DPPLog(@"%@", response);
//    }];

    NSArray * classes = @[@"DishCategory", @"Dish", @"Order", @"OrderedDish", @"Plan", @"Table"];
    syncer.delegate = self;
    for (NSString * class in classes) {
        classesToSync++;
        [syncer syncClassNamed:class];
    }
    [syncer syncDeletedObjectsOfClasses:classes];
}

- (void)syncEnded {
    NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
    NSError * error;
    
    if (![context save:&error])
        PPLog(@"%@", error);
    DLog(@"END SYNC");
    // should hide a loader
}

- (void)loadDatabase {
    // http://stackoverflow.com/questions/10417353/how-to-deal-with-a-multiple-user-database
    DPPLog(@"Should try to load the correct db with coredata");
}

- (void)hidePanel {
    if (panelShown)
        [self showPanel];
}

#pragma mark - IBAction methods

- (IBAction)showPanel {
    panelShown = !panelShown;
    
    [UIView animateWithDuration:0.4 animations:^{
        CGRect frame = panelView.frame;
        frame.origin.x += frame.size.width * (panelShown ? 1 : -1);
        panelView.frame = frame;
        overlay.alpha = panelShown ? 1 : 0;
    }];
}

- (IBAction)buttonClicked:(id)sender {
    [controller.view removeFromSuperview];

    mainView.hidden = YES;
    if (sender == takeOrderButton || sender == orderButton) {
        controller = [[CommandViewController alloc] initWithNibName:@"CommandView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"order", @"").capitalizedString;
    } else if (sender == historyButton) {
        controller = nil;
        titleLabel.text = NSLocalizedString(@"history", @"").capitalizedString;
    } else if (sender == notificationsButton) {
        controller = [[NotificationViewController alloc] initWithNibName:@"NotificationView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"notifications", @"").capitalizedString;
    } else if (sender == aboutButton) {
        controller = [[AboutViewController alloc] initWithNibName:@"AboutView" bundle:nil];
        titleLabel.text = NSLocalizedString(@"about", @"").capitalizedString;
    } else { // MainButton or unknown button
        controller = nil;
        mainView.hidden = NO;
        titleLabel.text = NSLocalizedString(@"Main Page", @"");
    }

    [self hidePanel];
    CGRect frame = centralView.frame;
    frame.origin = CGPointZero;
    controller.view.frame = frame;
    [centralView addSubview:controller.view];
}

- (IBAction)logOut {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - ParseSyncer delegate methods

- (void)syncDidFailWithError:(NSError *)error forClass:(NSString *)className {
    PPLog(@"%@ > %@", className, error);
    if (!--classesToSync)
        [self syncEnded];
}

- (void)syncDidStopForClass:(NSString *)className {
    DLog(@"sync OK %@", className);
    if (!--classesToSync)
        [self syncEnded];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return ((tableView == lastOrdersTableView) ? lastOrders.count : lastNotifications.count);
}

- (UITableViewCell *)orderCellAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"OrderCell";

    OrderCell * cell = [lastOrdersTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:@"OrderCell" owner:self options:nil][0];
        cell = [[OrderCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }

    [cell setOrder:lastOrders[indexPath.row]];
    return cell;
}

- (UITableViewCell *)notificationCellAtIndexPath:(NSIndexPath *)indexPath {
    static NSString * CellIdentifier = @"notificationCell";
    
    NotificationCell * cell = [lastNotificationsTableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
        cell = [[NotificationCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    [cell setNotification:lastNotifications[indexPath.row]];
    return cell;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return ((tableView == lastOrdersTableView) ? [self orderCellAtIndexPath:indexPath] : [self notificationCellAtIndexPath:indexPath]);
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
